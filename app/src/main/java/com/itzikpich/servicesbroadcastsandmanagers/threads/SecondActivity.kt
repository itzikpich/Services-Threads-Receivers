package com.itzikpich.servicesbroadcastsandmanagers.threads

import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import com.itzikpich.servicesbroadcastsandmanagers.R

/**
 * Event loop concept - need to keep a thread alive
 * Thread - start() -> run() -> terminate, when a thread terminated cannot use same instance to run again, Exception will be thrown
 * MainThread - start() -> run() , stays alive waiting for messages,
 * provided by MessageQueue which is infinite Java "for loop",
 * a Looper run on the queue and do work sequentially,
 * a Handler is responsible for providing tasks for the MessageQueue, we post messages from background thread to the Main thread,
 * we can specify a time or a delay to the messages, then the looper loops the MessageQueue and getting the current messages or messages that time in the past,
 * then he dispatch it to the Handler, which is the Handler's second responsibility, to execute the message.
 * MessageQueue, Handler and Looper are Android Classes and belong to the Android Framework
 * */

private const val TAG = "SecondActivity"

class SecondActivity: AppCompatActivity() {

    val looperThread = ExampleLooperThread()
    val handlerThread = ExampleHandlerThread()
    lateinit var mainHandler: Handler // handler associated with Main Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: $looperThread, $handlerThread")
        setContentView(R.layout.activity_second)
        mainHandler = Handler(Looper.getMainLooper())
        looperThread.start()
        handlerThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        // this will crash if looperHandler wasn't initialized
        looperThread.handler?.looper?.quit() // kill the thread to avoid leaking the activity
        handlerThread.quitSafely()
    }

    fun stopThread(view: View) {
        Log.d(TAG, "stopThread")
        Log.d(TAG, "${looperThread.handler?.looper}, ${looperThread.mLooper}")
        looperThread.handler?.looper?.quit()
    }

    fun taskA(view: View) {
        /**
         * difference between sending runnable and message to thread:
         * runnable - passing work to do
         * message - passing raw data
         * */

        // in order the Handler know what to do with the message we need to create custom Handler like ExampleHandler
        Message.obtain().let { message ->
            message.what = ExampleHandler.TASK_A
            looperThread.handler?.sendMessage(message)
        }
//        looperThread.handler?.post { // send something from main thread to looper thread
//            Log.d(TAG, "${looperThread.handler?.looper}, ${looperThread.mLooper}")
//            (1..10).forEach {
//                Log.d(TAG, "run: $it")
//                SystemClock.sleep(1000)
//            }
//        }
    }

    fun taskB(view: View) {
        Message.obtain().let { message ->
            message.what = ExampleHandler.TASK_B
            looperThread.handler?.sendMessage(message)
        }
    }

    fun startHandlerThreadWork(view: View) {
        val message = Message.obtain().apply {
            target = handlerThread.handler // you can set target and than call "message.sentToTarget()"
            what = ExampleHandler.TASK_A
            arg1 = 23
            obj = "object"
            data = bundleOf("key" to "value")
        }
//        handlerThread.handler?.sendMessage(message)
        message.sendToTarget() // same as sendMessage() with a handler target

//        handlerThread.handler?.post(ExampleRunnable("run 1"))
//        handlerThread.handler?.postDelayed(ExampleRunnable("run 2"), 2000)
//        handlerThread.handler?.post(ExampleRunnable("run 3"))
//        handlerThread.handler?.postAtFrontOfQueue(ExampleRunnable("run 4"))

        handlerThread.handler?.post {
            (1..3).forEach {
                SystemClock.sleep(1000)
                Log.d(TAG, "$it")
                /**
                 * to do ui related stuff, you must run on UI Thread
                 * the samples below are similar
                 * */
//                mainHandler.post {
//                    findViewById<AppCompatButton>(R.id.threadText).text = (3 - it).toString()
//                }
//                findViewById<AppCompatButton>(R.id.threadText).apply {
//                    post {
//                        text = (3 - it).toString()
//                    }
//                }
                runOnUiThread() {

                    Log.d(TAG, "update ui")
                    findViewById<AppCompatButton>(R.id.threadText).text = "$it"
                } // will leak when activity get destroyed

            }
        }

    }

    fun finish(view: View) {
        this.finish()
    }

}

class ExampleRunnable(val msg: String) : Runnable { // create this class outside of activity's scope, to not leak the activity
    override fun run() {
        (1..3).forEach {
            Log.d(TAG, "$msg, $it")
            SystemClock.sleep(500)
        }
    }
}