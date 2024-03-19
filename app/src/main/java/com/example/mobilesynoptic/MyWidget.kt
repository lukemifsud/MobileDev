package com.example.mobilesynoptic

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews


class MyWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ){
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if(intent.action == Intent.ACTION_BATTERY_CHANGED || intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, MyWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
        } else if (intent.action == Intent.ACTION_POWER_CONNECTED)
        {
            recordChargingEvent(context, true)
        } else if (intent.action == Intent.ACTION_POWER_DISCONNECTED){
            recordChargingEvent(context, false)
        }

    }

    private fun recordChargingEvent(context: Context, isConnected: Boolean) {
        val sharedPreferences = context.getSharedPreferences("BatteryPreferences", Context.MODE_PRIVATE)

        if(isConnected){
            val editor = sharedPreferences.edit()
            editor.putLong("LastChargeTime", System.currentTimeMillis())
            editor.apply()
        }
    }

     private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int){
        val chargeStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val level = chargeStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = chargeStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPerc = level / scale.toFloat()

        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        //update battery icon
        when {
            batteryPerc > 0.75 -> views.setImageViewResource(R.id.batteryIcon, R.drawable.green_symbol)
            batteryPerc > 0.45 -> views.setImageViewResource(R.id.batteryIcon, R.drawable.orange_symbol)
            else -> views.setImageViewResource(R.id.batteryIcon, R.drawable.red_symbol)
        }

        val weatherAlert = AppWidgetManager.getInstance(context).getAppWidgetOptions(appWidgetId).getString("weatherAlert", null)
        if (weatherAlert != null) {
            // Handle the display of weather alert in the widget
            // Example: Update a TextView with the weather alert
            views.setViewVisibility(R.id.weatherAlertText, View.VISIBLE)
            views.setTextViewText(R.id.weatherAlertText, weatherAlert)
            // Optionally, reset the weather alert to null after displaying it
            val options = Bundle()
            options.putString("weatherAlert", null)
            appWidgetManager.updateAppWidgetOptions(appWidgetId, options)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)

    }


}