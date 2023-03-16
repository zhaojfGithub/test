package com.zhao.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhao.mylibrary.MyStringUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


    val isState = MutableLiveData<Boolean>()


    fun testLaunch() {
        try {
            viewModelScope.launch {
                MyStringUtil.ndkString()
                throw Exception("这是一个错误")
                delay(200)
                Log.e("MainViewModel","测试")
            }
        }catch (e:Exception){
            Log.e("MainViewModel",e.toString())
        }
    }


    /**
     * login
     * user
     * ku cui guan li
     * yu yin
     * common
     * base
     */
    private fun testStart1(){
        val builder = OkHttpClient.Builder()
            .build()
    }
}