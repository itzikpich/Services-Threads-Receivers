package com.itzikpich.servicesbroadcastsandmanagers.threads

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.flow.asFlow

private const val TAG = "ExampleLooperThread"

class ExampleLooperThread: Thread() {

    var handler: ExampleHandler? = null
    private set
    var mLooper: Looper? = null
    private set

    override fun run() {
        Log.d(TAG, "run started")
        Looper.prepare() // adds looper to this thread and automatically create messageQueue
        Looper.myLooper()?.let { looper ->
            mLooper = looper
            Log.d(TAG, looper.toString())
            handler = ExampleHandler(looper)
            Looper.loop() // this starts an infinite for loop
            Log.d(TAG, "end of run") // when calling quit() everything after Looper.Loop() will call
        }
    }

    fun quit() {
        Log.d(TAG, Looper.myLooper().toString())
        Looper.myLooper()?.quit()
//        Looper.myLooper()?.quitSafely() // quit after dispatching current task in that current or past timestamp
    }

//    override fun run() {
//        (1..5).forEach {
//            Log.d(TAG, "run: $it")
//            SystemClock.sleep(1000)
//        }
//        Log.d(TAG, "end of run()")
//    }

}