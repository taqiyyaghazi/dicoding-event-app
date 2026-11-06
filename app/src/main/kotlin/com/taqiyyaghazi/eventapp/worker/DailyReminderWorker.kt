package com.taqiyyaghazi.eventapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.taqiyyaghazi.eventapp.R
import com.taqiyyaghazi.eventapp.data.remote.retrofit.ApiConfig
import com.taqiyyaghazi.eventapp.ui.detail.DetailActivity

class DailyReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val response = ApiConfig.getApiService().getEvents(active = -1)
            val nearestEvent = response.listEvents.firstOrNull()

            if (nearestEvent != null) {
                showNotification(
                    applicationContext,
                    "Event Terdekat: ${nearestEvent.name}",
                    "Waktu: ${nearestEvent.beginTime}",
                    nearestEvent.id.toString()
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        eventId: String
    ) {
        val channelId = "daily_event_reminder_channel"
        val channelName = "Daily Event Reminder"
        val notificationId = 101

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel pengingat event harian Dicoding"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val detailIntent = Intent(context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
        }

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(detailIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
