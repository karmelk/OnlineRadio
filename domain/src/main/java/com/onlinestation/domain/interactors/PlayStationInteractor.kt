package com.onlinestation.domain.interactors

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat

interface PlayStationInteractor {
    fun getRoot():String
    fun getMediaItems(): List<MediaBrowserCompat.MediaItem>?
    fun getMetadata(context: Context, mediaId: String?)
}