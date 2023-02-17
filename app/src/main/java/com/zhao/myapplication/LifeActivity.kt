package com.zhao.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 *创建时间： 2023/2/15
 *编   写：  zjf
 *页面功能:
 */
class LifeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLog("onCreate")
    }

    override fun onResume() {
        super.onResume()
        mLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        mLog("onPause")
    }

    override fun onStart() {
        super.onStart()
        mLog("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        mLog("onRestart")
    }

    override fun onStop() {
        super.onStop()
        mLog("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mLog("onDestroy")
    }

    private fun mLog(msg: String) {
        Log.e("LifeActivity", msg)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }
}