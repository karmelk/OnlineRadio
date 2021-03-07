package com.onlinestation.service

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
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
import com.onlinestation.utils.parseM3UToString
import kotlinx.coroutines.*
import saschpe.exoplayer2.ext.icy.IcyHttpDataSourceFactory

class MediaPlayerAdapter(
    private val radioLibrary: PlayingRadioLibrary,
    private val context: Context,
    private val mPlaybackInfoListener: PlaybackInfoListener
) {
    private var mCurrentMedia: MediaMetadataCompat? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var mState = 0
    private var mCurrentMediaPlayedToCompletion = false
    private var mSeekWhileNotPlaying = -1

    private fun initExoPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context, DefaultRenderersFactory(context), DefaultTrackSelector(),
            DefaultLoadControl()
        )

    }

    fun playFromMedia(metadata: MediaMetadataCompat?) {
        mCurrentMedia = metadata
        release()
        initExoPlayer()
        val stationUrl = metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
        stationUrl?.let {
            GlobalScope.launch(Dispatchers.Default) { // CoroutineScope
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
        // return mMediaPlayer != null && mMediaPlayer!!.isPlaying
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


    private fun loadRadio(urlString: String) {
        val mp = "https://www.internet-radio.com/servers/tools/playlistgenerator/?u=http://uk6.internet-radio.com:8179/listen.pls&t=.m3u"
        var url: String? = parseM3UToString(mp, "m3u")
        if(url==null){
            url="http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99473570"
        }
        val mediaSource = extractMediaSourceFromUri(Uri.parse(url))

//https://s2.ssl-stream.com:8190/radio.mp3
        exoPlayer?.stop()
        exoPlayer?.apply {
            // AudioAttributes here from exoplayer package !!!
            val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            // In 2.9.X you don't need to manually handle audio focus :D
            setAudioAttributes(attr, true)
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

    fun test(uri: Uri): ExtractorMediaSource {
        // ... exoPlayer instance already created

// Custom HTTP data source factory which requests Icy metadata and parses it if
// the stream server supports it
        val icyHttpDataSourceFactory = IcyHttpDataSourceFactory.Builder("Exo")
            .setIcyHeadersListener { icyHeaders ->
                Log.d("XXX", "onIcyHeaders: %s".format(icyHeaders.toString()))
            }
            .setIcyMetadataChangeListener { icyMetadata ->
                Log.d("XXX", "onIcyMetaData: %s".format(icyMetadata.toString()))
            }
            .build()

// Produces DataSource instances through which media data is loaded
        val dataSourceFactory = DefaultDataSourceFactory(context, null, icyHttpDataSourceFactory)

// The MediaSource represents the media to be played
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(uri)

        return mediaSource
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
            onStop()
            Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            super.onPlayerError(error)
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            super.onTracksChanged(trackGroups, trackSelections)
        }
    }
}