package com.silai.app.database

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.silai.app.R

/**
 
 * NotificationHelper.kt
 * ======================
 * Handles all app notifications.
 * Uses NotificationCompat (works on all Android versions).
 *
 */
object NotificationHelper {

    private const val CHANNEL_ID = "silai_orders"
    private const val CHANNEL_NAME = "Order Updates"
    private const val CHANNEL_DESC = "Notifications for order status changes"

    // Call this once at app startup to register the notification channel
    fun createNotificationChannel(context: Context) {
        // Channels required only on Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // Show a notification with a title and message
    fun showOrderNotification(context: Context, title: String, message: String, notifId: Int = 1) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)  // dismiss when tapped
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notifId, notification)
    }
}