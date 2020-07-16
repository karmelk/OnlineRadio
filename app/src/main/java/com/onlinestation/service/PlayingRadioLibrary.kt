package com.onlinestation.service

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.onlinestation.domain.R
import java.util.*
import java.util.concurrent.TimeUnit

class PlayingRadioLibrary {

    companion object {
        private val music = TreeMap<String, MediaMetadataCompat>()
        private val musicFileName = HashMap<String, String>()
        private val albumRes = HashMap<String, Int>()
        fun init() {
            createMediaMetadataCompat(
                "Jazz_In_Paris",
                "Jazz in Paris",
                "Media Right Productions",
                "Jazz & Blues",
                "Jazz",
                103,
                TimeUnit.SECONDS,
                "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99473570",
                R.drawable.album_jazz_blues,
                "album_jazz_blues"
            )
            createMediaMetadataCompat(
                "The_Coldest_Shoulder",
                "The Coldest Shoulder",
                "The 126ers",
                "Youtube Audio Library Rock 2",
                "Rock",
                160,
                TimeUnit.SECONDS,
                "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99488548",
                R.drawable.album_youtube_audio_library_rock_2,
                "album_youtube_audio_library_rock_2"
            )
            createMediaMetadataCompat(
                    "Jazz_In_Paris2",
            "Jazz in Paris2",
            "Media Right Productions2",
            "Jazz & Blues2",
            "Jazz2",
            103,
            TimeUnit.SECONDS,
            "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=99497996",
            R.drawable.album_jazz_blues,
            "album_jazz_blues2"
            )
        }

        fun getRoot(): String {
            return "root"
        }

        private fun getAlbumArtUri(albumArtResName: String): String? {
            return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    "com.onlineradio" + "/drawable/" + albumArtResName
        }

        fun getMusicFilename(mediaId: String?): String? {
            return if (musicFileName.containsKey(
                    mediaId
                )
            ) musicFileName[mediaId] else null
        }

        private fun getAlbumRes(mediaId: String): Int {
            return if (albumRes.containsKey(mediaId)) albumRes[mediaId]!! else 0
        }

        fun getAlbumBitmap(
            context: Context,
            mediaId: String
        ): Bitmap {
            return BitmapFactory.decodeResource(
                context.resources,
                getAlbumRes(
                    mediaId
                )
            )
        }

        fun getMediaItems(): List<MediaBrowserCompat.MediaItem>? {
            val result: MutableList<MediaBrowserCompat.MediaItem> =
                ArrayList()
            for (metadata in music.values) {
                result.add(
                    MediaBrowserCompat.MediaItem(
                        metadata.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                    )
                )
            }
            return result
        }

        fun getMetadata(
            context: Context,
            mediaId: String
        ): MediaMetadataCompat? {
            val metadataWithoutBitmap: MediaMetadataCompat? = music[mediaId]
            val albumArt: Bitmap =
                getAlbumBitmap(
                    context,
                    mediaId
                )
            // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
            // We don't set it initially on all items so that they don't take unnecessary memory.
            val builder = MediaMetadataCompat.Builder()
            for (key in arrayOf(
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                MediaMetadataCompat.METADATA_KEY_ALBUM,
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                MediaMetadataCompat.METADATA_KEY_GENRE,
                MediaMetadataCompat.METADATA_KEY_TITLE
            )) {
                builder.putString(key, metadataWithoutBitmap?.getString(key))
            }
            metadataWithoutBitmap?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.let {
                builder.putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    it
                )
            }
            builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
            return builder.build()
        }


        private fun createMediaMetadataCompat(
            mediaId: String,
            title: String,
            artist: String,
            album: String,
            genre: String,
            duration: Long,
            durationUnit: TimeUnit,
            musicFilename: String,
            albumArtResId: Int,
            albumArtResName: String
        ) {
            music[mediaId] = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    TimeUnit.MILLISECONDS.convert(duration, durationUnit)
                )
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                    getAlbumArtUri(
                        albumArtResName
                    )
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                    getAlbumArtUri(
                        albumArtResName
                    )
                )
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .build()
            albumRes[mediaId] = albumArtResId
            musicFileName[mediaId] = musicFilename
        }


    }

}