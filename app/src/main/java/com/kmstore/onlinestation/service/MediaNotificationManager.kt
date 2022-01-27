package com.kmstore.onlinestation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.app.NotificationCompat.MediaStyle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.kmstore.onlinestation.activity.MainActivity
import com.kmstore.onlinestation.R

class MediaNotificationManager(private val service: RadioService) {

    companion object {
        const val NOTIFICATION_ID = 412
    }

    private val TAG = MediaNotificationManager::class.java.simpleName
    private val CHANNEL_ID = "com.example.android.musicplayer.channel"
    private val REQUEST_CODE = 501
    private lateinit var mPlayAction: NotificationCompat.Action
    private lateinit var mPauseAction: NotificationCompat.Action
    private lateinit var mNextAction: NotificationCompat.Action
    private lateinit var mPrevAction: NotificationCompat.Action
    private lateinit var mNotificationManager: NotificationManager

    init {
        initMediaManager(service)
    }

    private fun initMediaManager(service: RadioService) {
        mNotificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mPlayAction = NotificationCompat.Action(
            R.drawable.ic_play_arrow_white_24dp,
            service.getString(R.string.label_play),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_PLAY
            )
        )
        mPauseAction = NotificationCompat.Action(
            R.drawable.ic_pause_white_24dp,
            service.getString(R.string.label_pause),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_STOP
            )
        )
        mNextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next_white_24dp,
            service.getString(R.string.label_next),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )
        mPrevAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous_white_24dp,
            service.getString(R.string.label_previous),
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                service,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )
        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        mNotificationManager.cancelAll()
    }

    fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    fun getNotificationManager(): NotificationManager? {
        return mNotificationManager
    }

    fun getNotification(
        metadata: MediaMetadataCompat?,
        state: PlaybackStateCompat?,
        token: MediaSessionCompat.Token?
    ): Notification? {
        val isPlaying = state?.state == PlaybackStateCompat.STATE_PLAYING
        val description = metadata?.description
        val builder: NotificationCompat.Builder =
            buildNotification(state, token, isPlaying, description)
        return builder.build()
    }

    private fun buildNotification(
        state: PlaybackStateCompat?,
        token: MediaSessionCompat.Token?,
        isPlaying: Boolean,
        description: MediaDescriptionCompat?
    ): NotificationCompat.Builder {

        if (isAndroidOOrHigher()) {
            createChannel()
        }
        val builder =
            NotificationCompat.Builder(
                service,
                CHANNEL_ID
            ).setStyle(
                MediaStyle()
                    .setMediaSession(token)
                    .setShowActionsInCompactView(0, 1, 2) // For backwards compatibility with Android L and earlier.
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            service,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
                .setColor(ContextCompat.getColor(service, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_stat_image_audiotrack) // Pending intent that is fired when user clicks on notification.
                .setContentIntent(createContentIntent()) // Title - Usually Song name.
                .setContentTitle(description?.title) // Subtitle - Usually Artist name.
                .setContentText(description?.subtitle)

                // When notification is deleted (when playback is paused and notification can be
                // deleted) fire MediaButtonPendingIntent with ACTION_STOP.
                .setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service, PlaybackStateCompat.ACTION_STOP
                    )
                ) // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // If skip to next action is enabled.
        state?.let {
            if (it.actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS != 0L) {
                builder.addAction(mPrevAction)
            }
            builder.addAction(if (isPlaying) mPauseAction else mPlayAction)

            // If skip to prev action is enabled.
            if (it.actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT != 0L) {
                builder.addAction(mNextAction)
            }
        }


        return builder
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            val name: CharSequence = "MediaSession"
            // The user-visible description of the channel.
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
            Log.d(TAG, "createChannel: New channel created")
        } else {
            Log.d(
                TAG,
                "createChannel: Existing channel reused"
            )
        }
    }

    private fun isAndroidOOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private fun createContentIntent(): PendingIntent? {
        val openUI = Intent(service, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            service,
            REQUEST_CODE,
            openUI,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

}