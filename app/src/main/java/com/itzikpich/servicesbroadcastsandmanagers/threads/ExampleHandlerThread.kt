package com.itzikpich.servicesbroadcastsandmanagers.threads

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process

class ExampleHandlerThread: HandlerThread("ExampleHandlerThread", Process.THREAD_PRIORITY_BACKGROUND) {

    var handler: Handler? = null
    private set

    override fun onLooperPrepared() {
        handler = Looper.myLooper()?.let {
            ExampleHandler(it)
        }
    }

}