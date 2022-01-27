package com.kmstore.onlinestation.service

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat

import com.kmstore.onlinestation.service.MediaNotificationManager.Companion.NOTIFICATION_ID
import org.koin.android.ext.android.inject
import java.util.*


class RadioService : MediaBrowserServiceCompat() {
    companion object {
        private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"
        private const val MY_MEDIA_ROOT_ID = "media_root_id"
    }

    private val TAG = RadioService::class.java.simpleName
    private lateinit var mSession: MediaSessionCompat
    private lateinit var mMediaNotificationManager: MediaNotificationManager
    private var mServiceInStartedState = false
    private lateinit var mPlayback: MediaPlayerAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override fun onCreate() {
        super.onCreate()

        // Create a new MediaSession.
        mSession = MediaSessionCompat(this, "RadioService")

        mSession.setCallback(MediaSessionCallback())
        mSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        sessionToken = mSession.sessionToken
        mMediaNotificationManager =
            MediaNotificationManager(this)
        mPlayback = MediaPlayerAdapter(
            this,
            MediaPlayerListener()
        )
        Log.d(TAG, "onCreate: MusicService creating MediaSession, and MediaNotificationManager")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        mMediaNotificationManager.onDestroy()
        mPlayback.onStop()
        mSession.release()
        Log.d(TAG, "onDestroy: MediaPlayerAdapter stopped, and MediaSession released")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {

        // Clients can connect, but this BrowserRoot is an empty hierachy
        // so onLoadChildren returns nothing. This disables the ability to browse for content.
        return BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: Result<List<MediaBrowserCompat.MediaItem?>?>
    ) {
        //  Browsing not allowed
        if (MY_EMPTY_MEDIA_ROOT_ID == parentMediaId) {
            result.sendResult(null)
            return
        }
        // Assume for example that the music catalog is already loaded/cached.
        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()
        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == parentMediaId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        result.sendResult(mediaItems)

    }

    // MediaSession Callback: Transport Controls -> MediaPlayerAdapter
    inner class MediaSessionCallback : MediaSessionCompat.Callback() {

        private var mPreparedMedia: MediaMetadataCompat? = null

        override fun onPlay() {
            mPreparedMedia?.let {
                mPlayback.playFromMedia(mPreparedMedia)
                Log.d(TAG, "onPlayFromMediaId: MediaSession active")
            }
        }

        override fun onStop() {
            mPlayback.onStop()
            mSession.isActive = false
        }

        override fun onSkipToNext() {
            mPreparedMedia = null
            mPreparedMedia = playingRadioLibrary.next
            mSession.setMetadata(mPreparedMedia)
            if (!mSession.isActive) {
                mSession.isActive = true
            }
            onPlay()
        }

        override fun onSkipToPrevious() {
            mPreparedMedia = null
            mPreparedMedia = playingRadioLibrary.previous
            mSession.setMetadata(mPreparedMedia)
            if (!mSession.isActive) {
                mSession.isActive = true
            }
            onPlay()
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            mPreparedMedia = playingRadioLibrary.getMetadata(mediaId!!)
            mSession.setMetadata(mPreparedMedia)
            if (!mSession.isActive) {
                mSession.isActive = true
            }
            onPlay()
        }

    }

    // MediaPlayerAdapter Callback: MediaPlayerAdapter state -> MusicService.
    inner class MediaPlayerListener : PlaybackInfoListener() {
        private var mServiceManager: ServiceManager

        init {
            mServiceManager = ServiceManager()
        }

        override fun onPlaybackStateChange(state: PlaybackStateCompat) {
            // Report the state to the MediaSession.
            mSession.setPlaybackState(state)
            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING -> mServiceManager.moveServiceToStartedState(state)
                PlaybackStateCompat.STATE_PAUSED -> mServiceManager.updateNotificationForPause(state)
                PlaybackStateCompat.STATE_STOPPED -> mServiceManager.moveServiceOutOfStartedState(
                    state
                )
            }
        }

        internal inner class ServiceManager {

            fun moveServiceToStartedState(state: PlaybackStateCompat) {
                val notification: Notification? = mMediaNotificationManager.getNotification(
                    mPlayback.getCurrentMedia(),
                    state,
                    sessionToken
                )

                if (!mServiceInStartedState) {
                    ContextCompat.startForegroundService(
                        this@RadioService,
                        Intent(this@RadioService, RadioService::class.java)
                    )
                    mServiceInStartedState = true
                }
                startForeground(NOTIFICATION_ID, notification)
            }

            fun updateNotificationForPause(state: PlaybackStateCompat) {
                stopForeground(false)
                val notification: Notification? = mMediaNotificationManager.getNotification(
                    mPlayback.getCurrentMedia(),
                    state,
                    sessionToken
                )
                mMediaNotificationManager.getNotificationManager()
                    ?.notify(NOTIFICATION_ID, notification)
            }

            fun moveServiceOutOfStartedState(state: PlaybackStateCompat) {
                stopForeground(true)
                stopSelf()
                mServiceInStartedState = false
            }
        }
    }

}