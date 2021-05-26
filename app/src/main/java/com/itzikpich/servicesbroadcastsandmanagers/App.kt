package com.itzikpich.servicesbroadcastsandmanagers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

const val CHANNEL_ID = "serviceChannel"
const val BACKGROUND_CHANNEL_ID = "backgroundServiceChannel"

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val backgroundNotificationChannel = NotificationChannel(BACKGROUND_CHANNEL_ID, "Background Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            getSystemService(NotificationManager::class.java).apply {
                createNotificationChannel(notificationChannel)
                createNotificationChannel(backgroundNotificationChannel)
            }
        }
    }

}