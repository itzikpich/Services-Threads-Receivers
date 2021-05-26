package com.itzikpich.servicesbroadcastsandmanagers.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.itzikpich.servicesbroadcastsandmanagers.R
import com.itzikpich.servicesbroadcastsandmanagers.threads.SecondActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    fun startService(view: View) {
        Intent(this, MainService::class.java).apply {
            this.putExtra("input", findViewById<AppCompatEditText>(R.id.edittext).text.toString())
        }.also {
//            startService(it) // must called when in foreground, when call from background a illegalStateException will be thrown
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)// must call service's startForeground() less than 5 seconds or the service will be killed
            } else {
                startService(it) // pre O behaviour
            }
        }

    }

    fun stopService(view: View) {
        Intent(this, MainService::class.java).apply {
            stopService(this)
        }
    }

    fun startJobIntentService(view: View) {
        Intent(this, MainJobIntentService::class.java).apply {
            putExtra("input", findViewById<AppCompatEditText>(R.id.edittext).text.toString())
        }.also {
            MainJobIntentService.enqueue(this, it)
        }
    }

    fun startJobService(view: View) {
        val componentName = ComponentName(this, MainJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_SERVICE_ID, componentName)
//            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPersisted(true) // should restart job after boot happened
//            .setPeriodic(15*60*1000) // less than 15 minutes // if not using setPeriodic, job run once
            .build()
        
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "startJobService: job scheduled")
        } else {
            Log.d(TAG, "startJobService: job scheduling failed")
        }
    }

    fun stopJobService(view: View) {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(JOB_SERVICE_ID)
        Log.d(TAG, "stopJobService: job canceled")
    }
    fun navigate(view: View) {
        startActivity(Intent(this, SecondActivity::class.java))
    }

}