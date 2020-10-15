package com.onlinestation.utils

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope


class lOADrADIO(val context: Context) {

    fun loadRadio() {

        val mp = "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99473570"
        val url: String? = parseM3UToString(mp, "m3u")
        val mediaSource =
            extractMediaSourceFromUri(Uri.parse(url))
        val exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context, DefaultRenderersFactory(context)
            , DefaultTrackSelector(),
            DefaultLoadControl()
        )
        exoPlayer.apply {
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

    val eventListener: Player.EventListener = object : Player.EventListener {

        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            Toast.makeText(context, "$playbackState+ $playWhenReady", Toast.LENGTH_SHORT).show()
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
            // super.onTracksChanged(trackGroups, trackSelections)
        }
    }
}

