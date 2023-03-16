package com.zhao.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

/**
 *创建时间： 2023/2/15
 *编   写：  zjf
 *页面功能:
 */
class LifeActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLog("onCreate,state = " + viewModel.isState.value.toString())
    }

    override fun onResume() {
        super.onResume()
        mLog("onResume,state = " + viewModel.isState.value.toString())
    }

    override fun onPause() {
        super.onPause()
        mLog("onPause,state = " + viewModel.isState.value.toString())
    }

    override fun onStart() {
        super.onStart()
        mLog("onStart,state = " + viewModel.isState.value.toString())
    }

    override fun onRestart() {
        super.onRestart()
        mLog("onRestart,state = " + viewModel.isState.value.toString())
    }

    override fun onStop() {
        super.onStop()
        mLog("onStop,state = " + viewModel.isState.value.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        mLog("onDestroy,state = " + viewModel.isState.value.toString())
    }

    private fun mLog(msg: String) {
        Log.e("LifeActivity", msg)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }
}