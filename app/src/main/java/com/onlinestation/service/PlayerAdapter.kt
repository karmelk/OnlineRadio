package com.onlinestation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

import android.support.v4.media.MediaMetadataCompat

abstract class PlayerAdapter(private val context: Context) {

    private val MEDIA_VOLUME_DEFAULT = 1.0f
    private val MEDIA_VOLUME_DUCK = 0.2f

    private val AUDIO_NOISY_INTENT_FILTER = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private var mAudioNoisyReceiverRegistered = false
    /*private val mAudioNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                if (isPlaying()) {
                    pause()
                }
            }
        }
    }*/

    private var mAudioManager: AudioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var mAudioFocusHelper: AudioFocusHelper

    private var mPlayOnAudioFocus = false
    init {
        mAudioFocusHelper = AudioFocusHelper()
    }

    abstract fun playFromMedia(metadata: MediaMetadataCompat?)

    abstract fun getCurrentMedia(): MediaMetadataCompat?

    abstract fun isPlaying(): Boolean

    fun play() {
        if (mAudioFocusHelper.requestAudioFocus()) {
           // registerAudioNoisyReceiver()
            onPlay()
        }
    }
    protected abstract fun onPlay()


    fun stop() {
        mAudioFocusHelper.abandonAudioFocus()
        //unregisterAudioNoisyReceiver()
        onStop()
    }

    /**
     * Called when the media must be stopped. The player should clean up resources at this
     * point.
     */
    protected abstract fun onStop()



    abstract fun setVolume(volume: Float)

   /* open fun registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            context.registerReceiver(mAudioNoisyReceiver, AUDIO_NOISY_INTENT_FILTER)
            mAudioNoisyReceiverRegistered = true
        }
    }

    open fun unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            context.unregisterReceiver(mAudioNoisyReceiver)
            mAudioNoisyReceiverRegistered = false
        }
    }*/

    /*
    * Helper class for managing audio focus related tasks.
    */
    private inner class AudioFocusHelper : AudioManager.OnAudioFocusChangeListener {
        fun requestAudioFocus(): Boolean {
            val result: Int = mAudioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        fun abandonAudioFocus() {
            mAudioManager.abandonAudioFocus(this)
        }

        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (mPlayOnAudioFocus && !isPlaying()) {
                        play()
                    } else if (isPlaying()) {
                        setVolume(MEDIA_VOLUME_DEFAULT)
                    }
                    mPlayOnAudioFocus = false
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> setVolume(MEDIA_VOLUME_DUCK)
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> if (isPlaying()) {
                    mPlayOnAudioFocus = true
                   // pause()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    mAudioManager.abandonAudioFocus(this)
                    mPlayOnAudioFocus = false
                    stop()
                }
            }
        }
    }
}