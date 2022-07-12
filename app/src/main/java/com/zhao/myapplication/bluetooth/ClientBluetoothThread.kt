package com.zhao.myapplication.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.util.*

/**
 *创建时间： 2022/7/12
 *编   写：  zjf
 *页面功能: 作为客户端连接的蓝牙线程
 */
@SuppressLint("MissingPermission")
class ClientBluetoothThread(private val uuid: UUID, device: BluetoothDevice) : Thread() {

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createInsecureRfcommSocketToServiceRecord(uuid)
    }

    override fun run() {
        mmSocket?.use {
            it.connect()
        }
    }

    fun cancel(){
        try {

        }catch (e:Exception){

        }
    }
}