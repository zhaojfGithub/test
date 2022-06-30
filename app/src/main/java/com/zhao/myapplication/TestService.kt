package com.zhao.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 *创建时间： 2022/6/30
 *编   写：  zjf
 *页面功能:
 */
class TestService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mLog("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mLog("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLog("onDestroy")
    }




    private fun mLog(msg:String){
        Log.e(this.javaClass.simpleName,msg)
    }

}