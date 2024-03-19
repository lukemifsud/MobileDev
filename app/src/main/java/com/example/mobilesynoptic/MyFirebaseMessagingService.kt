package com.example.mobilesynoptic

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.isNotEmpty().let {
            val weatherAlert = remoteMessage.data["weatherAlert"]
            weatherAlert?.let {
                handleWeatherAlert(applicationContext, it)
            }
        }





    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "FCMMessagingService"
    }

    private fun handleWeatherAlert(context: Context, alert: String) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, MyWidget::class.java) // Replace WeatherWidget with your actual AppWidgetProvider subclass
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        appWidgetIds.forEach { appWidgetId ->
            val options = Bundle()
            options.putString("weatherAlert", alert)
            appWidgetManager.updateAppWidgetOptions(appWidgetId, options)

            // Now, trigger an update on your widget to display the alert,
            // You can call your widget's update method directly if it's accessible
            // Or send an intent that your widget is set to listen for, to trigger the update
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

}