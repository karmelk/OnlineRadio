package com.onlinestation.service

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.onlinestation.service.MediaNotificationManager.Companion.NOTIFICATION_ID
import java.util.*

class RadioService : MediaBrowserServiceCompat() {

    private val TAG = RadioService::class.java.simpleName
    private lateinit var mSession: MediaSessionCompat

    private lateinit var mMediaNotificationManager: MediaNotificationManager
    private var mCallback: MediaSessionCallback? = null
    private var mServiceInStartedState = false
    private lateinit var mPlayback: PlayerAdapter

    override fun onCreate() {
        super.onCreate()

        // Create a new MediaSession.
        mSession = MediaSessionCompat(this, "RadioService")

        mCallback = MediaSessionCallback()
        mSession.setCallback(mCallback)
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
        mPlayback.stop()
        mSession.release()
        Log.d(TAG, "onDestroy: MediaPlayerAdapter stopped, and MediaSession released")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(PlayingRadioLibrary.getRoot(), null)
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: Result<List<MediaBrowserCompat.MediaItem?>?>
    ) {
        result.sendResult(PlayingRadioLibrary.getMediaItems())
    }

    // MediaSession Callback: Transport Controls -> MediaPlayerAdapter
    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        private val mPlaylist: MutableList<MediaSessionCompat.QueueItem> = ArrayList()
        private var mQueueIndex = -1
        private var mPreparedMedia: MediaMetadataCompat? = null
        override fun onAddQueueItem(description: MediaDescriptionCompat) {
            mPlaylist.add(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mQueueIndex == -1) 0 else mQueueIndex
            mSession.setQueue(mPlaylist)
            Log.i(TAG, "onAddQueueItem: $mQueueIndex")
        }

        override fun onRemoveQueueItem(description: MediaDescriptionCompat) {
            mPlaylist.remove(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mPlaylist.isEmpty()) -1 else mQueueIndex
            mSession.setQueue(mPlaylist)
            Log.i(TAG, "onRemoveQueueItem: $mQueueIndex")
        }

        override fun onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()) {
                // Nothing to play.
                return
            }
            val mediaId = mPlaylist[mQueueIndex].description.mediaId

            mPreparedMedia = PlayingRadioLibrary.getMetadata(this@RadioService, mediaId!!)
            mSession.setMetadata(mPreparedMedia)
            if (!mSession.isActive) {
                mSession.isActive = true
            }
        }

        override fun onPlay() {
            if (!isReadyToPlay) {
                // Nothing to play.
                return
            }
            if (mPreparedMedia == null) {
                onPrepare()
            }
            mPlayback.playFromMedia(mPreparedMedia)
            Log.d(TAG, "onPlayFromMediaId: MediaSession active")
        }

        override fun onStop() {
            mPlayback.stop()
            mSession.isActive = false
            mPreparedMedia = null
        }

        override fun onSkipToNext() {
            mQueueIndex = ++mQueueIndex % mPlaylist.size
            mPreparedMedia = null
            onPlay()
        }

        override fun onSkipToPrevious() {
            mQueueIndex = if (mQueueIndex > 0) mQueueIndex - 1 else mPlaylist.size - 1
            mPreparedMedia = null
            onPlay()
        }

        private val isReadyToPlay: Boolean
             get() = mPlaylist.isNotEmpty()
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