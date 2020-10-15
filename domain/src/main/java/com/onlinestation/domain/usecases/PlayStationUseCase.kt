package com.onlinestation.domain.usecases

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.onlinestation.domain.interactors.PlayStationInteractor

class PlayStationUseCase() : PlayStationInteractor {

    override fun getRoot(): String =""

    override fun getMediaItems(): List<MediaBrowserCompat.MediaItem>? = listOf()

    override fun getMetadata(context: Context, mediaId: String?){}


}