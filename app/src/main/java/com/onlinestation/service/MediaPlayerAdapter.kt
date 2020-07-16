package com.onlinestation.service

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.test.core.app.ActivityScenario.launch
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.onlinestation.utils.ParserM3UToURL
import com.onlinestation.utils.parseM3UToString
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import kotlin.coroutines.coroutineContext

class MediaPlayerAdapter(
    private val context: Context,
    private val mPlaybackInfoListener: PlaybackInfoListener
) : PlayerAdapter(context) {

    private var mFilename: String = ""
    //private var mMediaPlayer: MediaPlayer?=null
    private var mCurrentMedia: MediaMetadataCompat? = null

    private var exoPlayer: SimpleExoPlayer ?=null
    private var mState = 0
    private var mCurrentMediaPlayedToCompletion = false
    private var mSeekWhileNotPlaying = -1

    /*  private fun initializeMediaPlayer() {
          if (mMediaPlayer == null) {
              mMediaPlayer = MediaPlayer()
              mMediaPlayer!!.setOnCompletionListener(OnCompletionListener {
                  mPlaybackInfoListener.onPlaybackCompleted()

                  // Set the state to "paused" because it most closely matches the state
                  // in MediaPlayer with regards to available state transitions compared
                  // to "stop".
                  // Paused allows: seekTo(), start(), pause(), stop()
                  // Stop allows: stop()
                  setNewState(PlaybackStateCompat.STATE_PAUSED)
              })
          }
      }
  */
    private fun initExoPlayer(){
        exoPlayer= ExoPlayerFactory.newSimpleInstance(
                context, DefaultRenderersFactory(context)
                , DefaultTrackSelector(),
                DefaultLoadControl()
            )

    }

    override fun playFromMedia(metadata: MediaMetadataCompat?) {
        mCurrentMedia = metadata
        release()
        initExoPlayer()
        val mediaId = metadata?.description?.mediaId
        PlayingRadioLibrary.getMusicFilename(
            mediaId
        )?.let {
            play()
            GlobalScope.launch { // CoroutineScope
                loadRadio(it)
            }
        }
    }

    override fun getCurrentMedia(): MediaMetadataCompat? {
        return mCurrentMedia
    }

    /*
        private fun playFile(filename: String) {
            var mediaChanged = mFilename == null || filename != mFilename
            if (mCurrentMediaPlayedToCompletion) {
                // Last audio file was played to completion, the resourceId hasn't changed, but the
                // player was released, so force a reload of the media file for playback.
                mediaChanged = true
                mCurrentMediaPlayedToCompletion = false
            }
            if (!mediaChanged) {
                if (!isPlaying()) {
                    play()
                }
                return
            } else {
                release()
            }
            mFilename = filename
            initializeMediaPlayer()
            try {
                val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(mFilename)
                mMediaPlayer!!.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
            } catch (e: Exception) {
                throw RuntimeException("Failed to open file: $mFilename", e)
            }
            try {
                mMediaPlayer!!.prepare()
            } catch (e: Exception) {
                throw RuntimeException("Failed to open file: $mFilename", e)
            }
            play()
        }

        private fun release() {
            if (mMediaPlayer != null) {
                mMediaPlayer?.release()
                mMediaPlayer = null
            }
        }*/
    private fun release() {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
        }
    }
    override fun isPlaying(): Boolean {
        // return mMediaPlayer != null && mMediaPlayer!!.isPlaying
        return exoPlayer?.isPlayingAd!!
    }

    override fun onPlay() {
        /*   if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
               mMediaPlayer!!.start()

           }*/
        setNewState(PlaybackStateCompat.STATE_PLAYING)
    }

/*    override fun onPause() {
           *//*  if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
               mMediaPlayer!!.pause()

           }*//*
        release()
        setNewState(PlaybackStateCompat.STATE_PAUSED)
    }*/

    override fun onStop() {

        // Regardless of whether or not the MediaPlayer has been created / started, the state must
        // be updated, so that MediaNotificationManager can take down the notification.
        release()
        setNewState(PlaybackStateCompat.STATE_STOPPED)
    }


    override fun setVolume(volume: Float) {
        exoPlayer?.volume = volume
    }

    private fun setNewState(@PlaybackStateCompat.State newPlayerState: Int) {
        mState = newPlayerState

        // Whether playback goes to completion, or whether it is stopped, the
        // mCurrentMediaPlayedToCompletion is set to true.
        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true
        }

        // Work around for MediaPlayer.getCurrentPosition() when it changes while not playing.
        val reportPosition: Long
        if (mSeekWhileNotPlaying >= 0) {
            reportPosition = mSeekWhileNotPlaying.toLong()
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mSeekWhileNotPlaying = -1
            }
        } else {
            reportPosition = 0
            //  if (mMediaPlayer == null) 0 else mMediaPlayer!!.currentPosition.toLong()
        }
        val stateBuilder = PlaybackStateCompat.Builder()
        stateBuilder.setActions(getAvailableActions())//3379
        stateBuilder.setState(
            mState, //3
            reportPosition, //0
            1.0f,
            SystemClock.elapsedRealtime() //777942020
        )
        mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build())
    }

    @PlaybackStateCompat.Actions
    private fun getAvailableActions(): Long {
        var actions = (PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        actions = when (mState) {
            PlaybackStateCompat.STATE_STOPPED -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PAUSE)
            PlaybackStateCompat.STATE_PLAYING -> actions or (PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_SEEK_TO)
            PlaybackStateCompat.STATE_PAUSED -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_STOP)
            else -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE)
        }
        return actions
    }


   private fun loadRadio(urlString: String) {
        //val mp = "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99473570"
        val url: String? = parseM3UToString(urlString, "m3u")
        val mediaSource = extractMediaSourceFromUri(Uri.parse(url))

        exoPlayer?.stop()
        exoPlayer?.apply {
            // AudioAttributes here from exoplayer package !!!
            val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            // In 2.9.X you don't need to manually handle audio focus :D
            //   setAudioAttributes(attr, true)
            prepare(mediaSource)
            // THAT IS ALL YOU NEED
            playWhenReady = true
            addListener(eventListener)
        }
    }

    private fun extractMediaSourceFromUri(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(context, "Exo")
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(uri)
    }

   private val eventListener: Player.EventListener = object : Player.EventListener {

        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            setNewState(PlaybackStateCompat.STATE_PLAYING)
            //  Toast.makeText(context, "$playbackState+ $playWhenReady", Toast.LENGTH_SHORT).show()
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            Log.i("MessageExo", "" + error.message)
            Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            // super.onPlayerError(error)
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
             //super.onTracksChanged(trackGroups, trackSelections)
        }
    }
}