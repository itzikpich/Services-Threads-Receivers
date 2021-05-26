package com.itzikpich.servicesbroadcastsandmanagers.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

const val JOB_ID = 123

class MainJobIntentService: JobIntentService() { // must add WAKE_LOCK permission for lower than SDK 25

    val TAG = this::class.java.simpleName

    companion object {
        fun enqueue(context: Context, work: Intent) {
            enqueueWork(context, MainJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork on thread: ${Thread.currentThread().name}")
        val input = intent.getStringExtra("input")
        runBlocking { //CoroutineScope(Dispatchers.IO).launch {
            (1..10).asFlow().collect {
                Log.d(TAG, "onHandleWork: $input - $it")
                if (isStopped) return@collect // check if work stopped and onStopCurrentWork called
                delay(1000)
            }
        }
    }

    override fun onStopCurrentWork(): Boolean {
        Log.d(TAG, "onStopCurrentWork")
        return super.onStopCurrentWork() // should resume work or not, default true
    }

}