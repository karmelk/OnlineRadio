package com.nextidea.onlinestation.service

import android.support.v4.media.MediaMetadataCompat
import com.nextidea.onlinestation.domain.entities.StationItem

class PlayingRadioLibrary {
    private var stationList: MutableList<StationItem> = mutableListOf()
    private var currentItemIndex = 0
    private var currentStationFragmentName: String = ""

    fun updateLibraryStation(stations: List<StationItem>, fragmentName: String) {
        if (currentStationFragmentName != fragmentName) {
            currentStationFragmentName = fragmentName
            stationList.clear()
            stationList.addAll(stations)
        }
    }

    fun getCurrentStationIndex(): Int {
        return currentItemIndex
    }

    val next: MediaMetadataCompat
        get() {
            currentItemIndex = ++currentItemIndex % stationList.size
            return createMediaMetadata(
                currentStation.id.toString(),
                currentStation.name,
                currentStation.name,
                currentStation.stationUrl,
                currentStation.icon
            )
        }

    val previous: MediaMetadataCompat
        get() {
            currentItemIndex =
                if (currentItemIndex > 0) currentItemIndex - 1 else stationList.size - 1
            return createMediaMetadata(
                currentStation.id.toString(),
                currentStation.name,
                currentStation.name,
                currentStation.stationUrl,
                currentStation.icon
            )
        }

    private val currentStation: StationItem
        get() = stationList[currentItemIndex]

    val getCurrentStation: StationItem? get() = if(stationList.isNotEmpty()) stationList[currentItemIndex] else null

    fun getMetadata(
        mediaId: String
    ): MediaMetadataCompat {
        stationList.forEachIndexed { index, stationItemLocal ->
            if (stationItemLocal.id.toString() == mediaId) {
                currentItemIndex = index
                return@forEachIndexed
            }
        }
        return createMediaMetadata(
            currentStation.id.toString(),
            currentStation.name,
            currentStation.name,
            currentStation.stationUrl,
            currentStation.icon
        )
    }

    private fun createMediaMetadata(
        mediaId: String?,
        title: String?,
        genre: String?,
        stationUrl: String?,
        stationIconUrl: String?
    ): MediaMetadataCompat {

        val builder = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId?.run { this } ?: "")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, genre?.run { this } ?: "")
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title?.run { this } ?: "")
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, stationUrl?.run { this } ?: "")
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                stationIconUrl?.run { this } ?: "")

        return builder.build()
    }

    fun updateCurrentPlayedStation(item: StationItem) {
        val listItem = stationList.find { it.id == item.id } ?: return
        val index = stationList.indexOf(listItem)
        if (index != -1) {
            stationList = stationList.apply {
                this[index] = item
            }
        }
    }

}