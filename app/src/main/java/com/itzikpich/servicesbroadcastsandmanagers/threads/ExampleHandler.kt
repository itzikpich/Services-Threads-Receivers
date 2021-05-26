package com.itzikpich.servicesbroadcastsandmanagers.threads

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class ExampleHandler(looper: Looper) : Handler(looper) {

    companion object {
        private const val TAG = "ExampleHandler"
        const val TASK_A = 1
        const val TASK_B = 2
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what){
            TASK_A -> Log.d(TAG, "Task A executed, ${msg.data}, ${msg.arg1}, ${msg.obj}")
            TASK_B -> Log.d(TAG, "Task B executed")
        }
    }

}