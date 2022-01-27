package com.kmstore.onlinestation.service

import android.content.Context
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.kmstore.onlinestation.utils.parseM3UToString
import kotlinx.coroutines.*


class MediaPlayerAdapter(
    private val context: Context,
    private val mPlaybackInfoListener: PlaybackInfoListener
) {
    private var mCurrentMedia: MediaMetadataCompat? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var mState = 0
    private var mCurrentMediaPlayedToCompletion = false
    private var mSeekWhileNotPlaying = -1

    private fun initExoPlayer() {
        exoPlayer = SimpleExoPlayer.Builder(context).build()
    }

    fun playFromMedia(metadata: MediaMetadataCompat?) {
        mCurrentMedia = metadata
        release()
        initExoPlayer()
        val stationUrl = metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
        stationUrl?.let {
            GlobalScope.launch(Dispatchers.IO) { // CoroutineScope
                loadRadio(it)
            }
        }
    }

    fun getCurrentMedia(): MediaMetadataCompat? {
        return mCurrentMedia
    }

    private fun release() {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlayingAd!!
    }

    fun onStop() {
        // Regardless of whether or not the MediaPlayer has been created / started, the state must
        // be updated, so that MediaNotificationManager can take down the notification.
        release()
        setNewState(PlaybackStateCompat.STATE_STOPPED)
    }


    fun setVolume(volume: Float) {
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

    private suspend fun loadRadio(urlString: String) {
        val url = parseM3UToString(urlString, "m3u")
        //  val url: String = "http://uk5.internet-radio.com:8174/"

        val mediaItem: MediaItem = MediaItem.fromUri(url)
        withContext(Dispatchers.Main) {
            exoPlayer?.stop()
            exoPlayer?.apply {
                // AudioAttributes here from exoplayer package !!!
                val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MUSIC)
                    .build()
                // In 2.9.X you don't need to manually handle audio focus :D

                setAudioAttributes(attr, true)
                setMediaItem(mediaItem)
                prepare()
                play()
                playWhenReady = true
                addListener(eventListener)
            }
        }
    }

    private val eventListener: Player.Listener = object : Player.Listener {

        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            setNewState(PlaybackStateCompat.STATE_PLAYING)
             // Toast.makeText(context, "$playbackState+ $playWhenReady", Toast.LENGTH_SHORT).show()
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            onStop()
            Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            super.onPlayerError(error)
        }

    }
}