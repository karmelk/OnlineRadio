package com.onlinestation.service

import android.support.v4.media.session.PlaybackStateCompat

abstract class PlaybackInfoListener {
    abstract fun onPlaybackStateChange(state: PlaybackStateCompat)
    open fun onPlaybackCompleted() {}
}