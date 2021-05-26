package com.itzikpich.servicesbroadcastsandmanagers.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.itzikpich.servicesbroadcastsandmanagers.CHANNEL_ID
import com.itzikpich.servicesbroadcastsandmanagers.R
import com.itzikpich.servicesbroadcastsandmanagers.threads.SecondActivity


class MainService: Service() { // must add permission <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

    private val TAG = this::class.java.simpleName

    // first time start service called
    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }

    // every time stop service called
    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    // called when start service called
    // runs on UI thread
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        val input = intent.getStringExtra("input")
        val notificationIntent = Intent(this, SecondActivity::class.java) // this will start the activity
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 1010, notificationIntent, 0)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Main Service Notification")
            .setContentText(input)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification) // starts a foreground notification

        return START_NOT_STICKY
    }

    // for bounded components
    override fun onBind(intent: Intent?): IBinder? = null
}