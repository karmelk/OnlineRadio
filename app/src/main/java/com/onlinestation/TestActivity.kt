package com.onlinestation

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import com.onlinestation.service.MediaBrowserHelper
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.service.RadioService
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    private var mMediaBrowserHelper: MediaBrowserHelper? = null
    private var mIsPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initClickListener()
        PlayingRadioLibrary.init()
        mMediaBrowserHelper = MediaBrowserConnection(this)
        mMediaBrowserHelper?.registerCallback(MediaBrowserListener())
    }


    private fun initClickListener() {
        button_previous.setOnClickListener {
            mMediaBrowserHelper!!.getTransportControls()!!.skipToPrevious()
        }
        button_play.setOnClickListener {
            if (mIsPlaying) {
                mMediaBrowserHelper!!.getTransportControls()!!.pause()
            } else {
                mMediaBrowserHelper!!.getTransportControls()!!.play()
            }
        }
        button_next.setOnClickListener {
            mMediaBrowserHelper!!.getTransportControls()!!.skipToNext()
        }
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        // mSeekBarAudio.disconnectController()
        mMediaBrowserHelper!!.onStop()
    }

    private class MediaBrowserConnection(context: Context) : MediaBrowserHelper(context,
        RadioService::class.java) {

        override fun onConnected(mediaController: MediaControllerCompat) {
            // mSeekBarAudio.setMediaController(mediaController)
        }

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem?>
        ) {
            super.onChildrenLoaded(parentId, children)
            val mediaController = getMediaController()

            // Queue up all media items for this simple sample.
            for (mediaItem in children) {
                mediaController.addQueueItem(mediaItem!!.description)
            }

            // Call prepare now so pressing play just works.
            mediaController.transportControls.prepare()
        }
    }

    private inner class MediaBrowserListener : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(playbackState: PlaybackStateCompat?) {
            mIsPlaying = playbackState != null &&
                    playbackState.state == PlaybackStateCompat.STATE_PLAYING
            media_controls.isPressed = mIsPlaying
        }

        override fun onMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
            if (mediaMetadata == null) {
                return
            }
            song_title.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            song_artist.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            album_art.setImageBitmap(
                PlayingRadioLibrary.getAlbumBitmap(
                    this@TestActivity,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                )
            )
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
            super.onQueueChanged(queue)
        }

    }
}