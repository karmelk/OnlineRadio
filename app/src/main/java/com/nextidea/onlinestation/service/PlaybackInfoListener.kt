package com.nextidea.onlinestation.service

import android.support.v4.media.session.PlaybackStateCompat

abstract class PlaybackInfoListener {
    abstract fun onPlaybackStateChange(state: PlaybackStateCompat)
}