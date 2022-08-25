package com.zhao.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import java.io.File
import java.io.IOException

/**
 *创建时间： 2022/7/4
 *编   写：  zjf
 *页面功能:
 */
class MainViewModel : ViewModel() {


    private val data = MutableLiveData<String>()

    private val client: OkHttpClient = OkHttpClient()


    /**
     * 同步
     */
    fun testSynchronousOkhttp() {
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            for ((name, value) in response.headers) {
                mLog("name = $name value = $value")
            }
            mLog(response.body!!.string())
        }
    }


    private fun mLog(msg: String) {
        Log.e("MainViewModel", msg)
    }
}