package com.itzikpich.servicesbroadcastsandmanagers.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

const val JOB_SERVICE_ID = 3333

class MainJobService: JobService() {

    val TAG = this::class.java.simpleName
    private var jobCancelled = false

    override fun onStartJob(params: JobParameters?): Boolean { // normally runs on UI thread
        Log.d(TAG, "onStartJob: $this")
        doBackgroundWork(params)
//        return false // if the task is short and can be executed in this scope, JOB IS OVER
        return true // if you do long running on background thread
    }

    private fun doBackgroundWork(params: JobParameters?) {
        CoroutineScope(Dispatchers.Default).launch {
            (1..10).asFlow().collect {
                if (jobCancelled) return@collect
                Log.d(TAG, it.toString())
                delay(1000)
            }
            Log.d(TAG, "job finished")
            jobFinished(params, false) // if you want to reschedule work pass true
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true // Answers the question: "Should this job be retried?"
    }

}