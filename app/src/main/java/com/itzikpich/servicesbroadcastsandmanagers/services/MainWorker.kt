package com.itzikpich.servicesbroadcastsandmanagers.services

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*

private const val TAG = "MainWorker"

class MainWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun onStopped() {
        Log.d(TAG, "onStopped called")
        super.onStopped()
    }

    override fun doWork(): Result { // runs asynchronously on a background thread provided by WorkManager.
        Log.d(TAG, "doWork called")
        Log.d(TAG, "thread: ${Thread.currentThread().name}")
        val imageUriInput = inputData.getString("IMAGE_URI") ?: return Result.failure() //  get data passed from workRequest
        doBackgroundWork()
        return Result.success() // Indicate whether the work finished successfully with the Result
    }

    private fun doBackgroundWork() {
        Log.d(TAG, "doBackgroundWork called")
        runBlocking {
            Log.d(TAG, "thread: ${Thread.currentThread().name}")
            (1..2).forEach {
                Log.d(TAG, "doBackgroundWork: $it")
                delay(1000)
            }
        }
    }

}