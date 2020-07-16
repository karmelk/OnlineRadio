package com.onlinestation.service

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat

open class MediaBrowserHelper(
    private val context: Context,
    private val mMediaBrowserServiceClass: Class<out MediaBrowserServiceCompat?>
) {
    private val TAG = MediaBrowserHelper::class.java.simpleName
    private val mCallbackList: MutableList<MediaControllerCompat.Callback> = mutableListOf()
    private var mMediaBrowserConnectionCallback: MediaBrowserConnectionCallback
    private var mMediaControllerCallback: MediaControllerCallback
    private var mMediaBrowserSubscriptionCallback: MediaBrowserSubscriptionCallback
    private var mMediaBrowser: MediaBrowserCompat? = null
    private var mMediaController: MediaControllerCompat? = null

    init {
        mMediaBrowserConnectionCallback = MediaBrowserConnectionCallback()
        mMediaControllerCallback = MediaControllerCallback()
        mMediaBrowserSubscriptionCallback = MediaBrowserSubscriptionCallback()
    }

    fun onStart() {

        if (mMediaBrowser == null) {
            mMediaBrowser = MediaBrowserCompat(
                context,
                ComponentName(context, mMediaBrowserServiceClass),
                mMediaBrowserConnectionCallback,
                null
            )
            mMediaBrowser?.connect()
        }
        Log.d(TAG, "onStart: Creating MediaBrowser, and connecting")
    }

    fun onStop() {

        if (mMediaController != null) {
            mMediaController?.unregisterCallback(mMediaControllerCallback)
            mMediaController = null
        }
        if (mMediaBrowser != null && mMediaBrowser!!.isConnected) {
            mMediaBrowser!!.disconnect()
            mMediaBrowser = null
        }
        resetState()
        Log.d(
            TAG,
            "onStop: Releasing MediaController, Disconnecting from MediaBrowser"
        )
    }

    protected open fun onConnected(mediaController: MediaControllerCompat) {}

    protected open fun onChildrenLoaded(
        parentId: String,
        children: List<MediaBrowserCompat.MediaItem?>
    ) {
    }

    protected fun onDisconnected() {}
    protected fun getMediaController(): MediaControllerCompat {
        checkNotNull(mMediaController) { "MediaController is null!" }
        return mMediaController as MediaControllerCompat
    }


    private fun resetState() {
        performOnAllCallbacks(object :
            CallbackCommand {
            override fun perform(callback: MediaControllerCompat.Callback?) {
                mMediaController?.let {
                    val playbackState = it.playbackState
                    callback?.onPlaybackStateChanged(playbackState)
                }

            }
        })
        Log.d(TAG, "resetState: ")
    }

    fun getTransportControls(): MediaControllerCompat.TransportControls? {
        if (mMediaController == null) {
            Log.d(TAG, "getTransportControls: MediaController is null!")
            throw IllegalStateException("MediaController is null!")
        }
        return mMediaController!!.transportControls
    }

    open fun registerCallback(callback: MediaControllerCompat.Callback?) {
        callback?.let {
            mCallbackList.add(it)
            mMediaController?.apply {
                val metadata = mMediaController!!.metadata
                if (metadata != null) {
                    it.onMetadataChanged(metadata)
                }
                val playbackState = mMediaController!!.playbackState
                if (playbackState != null) {
                    it.onPlaybackStateChanged(playbackState)
                }
            }
            // Update with the latest metadata/playback state.
        }
    }

    private fun performOnAllCallbacks(command: CallbackCommand) {
        for (callback in mCallbackList) {
            if (callback != null) {
                command.perform(callback)
            }
        }
    }

    private interface CallbackCommand {
        fun perform(callback: MediaControllerCompat.Callback?)
    }

    private inner class MediaBrowserConnectionCallback :
        MediaBrowserCompat.ConnectionCallback() {
        // Happens as a result of onStart().
        override fun onConnected() {
            try {
                // Get a MediaController for the MediaSession.
                mMediaController = MediaControllerCompat(context, mMediaBrowser!!.sessionToken)

                mMediaController?.apply {
                    registerCallback(mMediaControllerCallback)
                    metadata?.let {
                        mMediaControllerCallback.onMetadataChanged(it)
                    }
                    playbackState?.let {
                        mMediaControllerCallback.onPlaybackStateChanged(it)
                    }
                }
                // Sync existing MediaSession state to the UI.

                this@MediaBrowserHelper.onConnected(mMediaController!!)
            } catch (e: RemoteException) {
                Log.d(TAG, String.format("onConnected: Problem: %s", e.toString()))
                throw RuntimeException(e)
            }
            mMediaBrowser!!.subscribe(mMediaBrowser!!.root, mMediaBrowserSubscriptionCallback)

        }
    }

    inner class MediaBrowserSubscriptionCallback :
        MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            this@MediaBrowserHelper.onChildrenLoaded(parentId, children)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            performOnAllCallbacks(object :
                CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback?) {
                    callback?.onMetadataChanged(metadata)
                }
            })
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            performOnAllCallbacks(object :
                CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback?) {
                    callback?.onPlaybackStateChanged(state)
                }
            })
        }

        // This might happen if the MusicService is killed while the Activity is in the
        // foreground and onStart() has been called (but not onStop()).
        override fun onSessionDestroyed() {
            resetState()
            mMediaController?.let {
                val playbackState = it.playbackState
                onPlaybackStateChanged(playbackState)
            }
            this@MediaBrowserHelper.onDisconnected()
        }
    }
}