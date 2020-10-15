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
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.google.android.exoplayer2.util.Util
import com.onlinestation.utils.parseM3UToString
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class MediaPlayerAdapter(
    private val context: Context,
    private val mPlaybackInfoListener: PlaybackInfoListener
) : PlayerAdapter(context) {

    private var mCurrentMedia: MediaMetadataCompat? = null

    private var exoPlayer: SimpleExoPlayer? = null
    private var mState = 0
    private var mCurrentMediaPlayedToCompletion = false

    private fun initExoPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context, DefaultRenderersFactory(context), DefaultTrackSelector(),
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
            GlobalScope.launch { // CoroutineScope
                loadRadio(it)
            }
        }
    }

    override fun getCurrentMedia(): MediaMetadataCompat? {
        return mCurrentMedia
    }

    private fun release() {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
        }
        setNewState(PlaybackStateCompat.STATE_PLAYING)
    }

    override fun isPlaying(): Boolean {
        return exoPlayer?.isPlayingAd!!
    }

    override fun onStop() {
        release()
        setNewState(PlaybackStateCompat.STATE_STOPPED)
    }


    override fun setVolume(volume: Float) {
        exoPlayer?.volume = volume
    }

    private fun setNewState(@PlaybackStateCompat.State newPlayerState: Int) {
        mState = newPlayerState

        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true
        }

        val stateBuilder = PlaybackStateCompat.Builder()
        stateBuilder.setActions(getAvailableActions())//3379
        stateBuilder.setState(
            mState, //3
            0, //0
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
            //  Toast.makeText(context, "$playbackState+ $playWhenReady", Toast.LENGTH_SHORT).show()
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    Log.i("MessageExo", "onPlayerStateChanged: STATE_BUFFERING " + playbackState)
                }
                Player.STATE_IDLE -> {

                }
                Player.STATE_READY -> {

                }
                Player.STATE_ENDED -> {
                    setNewState(PlaybackStateCompat.STATE_PLAYING)
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {

            Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            // super.onPlayerError(error)

            if (error.type == ExoPlaybackException.TYPE_SOURCE) {
                val cause: IOException = error.sourceException
                if (cause is HttpDataSourceException) {
                    // An HTTP error occurred.
                    val httpError = cause
                    // This is the request for which the error occurred.
                    val requestDataSpec = httpError.dataSpec

                    // It's possible to find out more about the error both by casting and by
                    // querying the cause.
                    if (httpError is InvalidResponseCodeException) {
                        // Cast to InvalidResponseCodeException and retrieve the response code,
                        // message and headers.

                        Log.i("MessageExo", "$httpError.responseMessage")
                    } else {
                        // Try calling httpError.getCause() to retrieve the underlying cause,
                        // although note that it may be null.
                    }
                }
            }
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            //super.onTracksChanged(trackGroups, trackSelections)
        }
    }
}