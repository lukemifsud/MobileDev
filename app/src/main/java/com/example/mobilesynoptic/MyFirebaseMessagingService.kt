package com.example.mobilesynoptic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Weather Alert"
        val message = remoteMessage.notification?.body ?: "Extreme weather warning"

        updateWidgetNotification(title,message)





    }

    private val CHANNEL_ID = "widget_update"
    private val CHANNEL_NAME = "Widget Update"
    private val CHANNEL_DESCRIPTION = "Notification about update to widget"
    private val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT

    private fun createNotificationChannel() {
        // Check if the Android Version is Oreo (API 26) or above since NotificationChannel is not supported in the older versions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun updateWidgetNotification(title: String, message: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //simple notification for demonstration

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(0, notification)

        //update widget

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val theWidget = ComponentName(this, MyWidget::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(theWidget)

        MyWidget.updateNotificationWidget(this, appWidgetManager, allWidgetIds, title, message)
    }



}