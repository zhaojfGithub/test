package com.zhao.myapplication

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val loadingView : LoadingTextView = findViewById(R.id.loadView)
        loadingView.start()
        //val testView : TextView1 = findViewById(R.id.loadView)
        //testView.start()
        Log.e("LoadingActivity","mainLooper,${mainLooper}")
        Log.e("LoadingActivity","Looper.myLooper(),${Looper.myLooper()}")
        Log.e("LoadingActivity","Looper.getMainLooper(),${Looper.getMainLooper()}")
        Log.e("LoadingActivity","onCreate,${Process.myTid()}")
        Thread{
            val message2 = Message.obtain()
            message2.obj = "第二个"
            handler.sendMessageAtTime(message2,SystemClock.uptimeMillis() + 1000)
            val message1 = Message.obtain()
            message1.obj = "第一个"
            handler.sendMessageAtTime(message1,SystemClock.uptimeMillis() + 800)
            Log.e("LoadingActivity","Thread,${Process.myTid()}")
            handler.postDelayed(Thread{
                val message3 = Message.obtain()
                message3.obj = "第三个"
                handler.sendMessage(message3)
                Log.e("LoadingActivity","NewThread,${Process.myTid()}")
            },500)
            val message4 = Message.obtain()
            message4.obj = "第四个"
            handler.sendMessageDelayed(message4,501)
        }.start()


        val thread = object : Thread(){
            override fun run() {
                super.run()
            }
        }
        thread.start()
    }

    private val handler  =  object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.e("LoadingView", "${msg.obj}")
            Log.e("LoadingActivity","Handler,${Process.myTid()}")
        }
    }
}