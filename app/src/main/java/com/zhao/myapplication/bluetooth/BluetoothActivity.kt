package com.zhao.myapplication.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.zhao.myapplication.R
import java.net.NetworkInterface
import java.util.*

class BluetoothActivity : AppCompatActivity() {

    private lateinit var permissionLaunch: ActivityResultLauncher<String>
    private lateinit var bluetoothLaunch: ActivityResultLauncher<String>
    private lateinit var receiver: BluetoothReceiver
    private lateinit var date: Date
    private var bluetoothAdapter: BluetoothAdapter? = null

    private lateinit var uuid: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        date = Date()
        //第一步，请求权限
        permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                date = Date()
                settingBluetooth()
            } else {
                permissionLaunch.launch(Manifest.permission.BLUETOOTH)
            }
        }
        bluetoothLaunch = registerForActivityResult(object : ActivityResultContract<String, Boolean>() {
            override fun createIntent(context: Context, input: String): Intent {
                return Intent(input)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    return true
                }
                return false
            }
        }) {
            if (!it) {
                bluetoothLaunch.launch(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            } else {
                settingBluetooth()
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        receiver = BluetoothReceiver { name, mac ->

        }
        registerReceiver(receiver, filter)
        if (PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)) {
            permissionLaunch.launch(Manifest.permission.BLUETOOTH)
        } else {
            settingBluetooth()
        }
    }

    private fun settingBluetooth() {
        val bluetoothManger: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManger.adapter
        if (bluetoothAdapter == null) {
            Log.e(this.javaClass.simpleName, "不支持蓝牙")
            return
        }
        if (!bluetoothAdapter!!.isEnabled) {
            bluetoothLaunch.launch(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        }
        /*被配对是指两台设备知晓彼此的存在，具有可用于身份验证的共享链路密钥，并且能够与彼此建立加密连接。
        被连接是指设备当前共享一个 RFCOMM 通道，并且能够向彼此传输数据。当前的 Android Bluetooth API 要求规定，
        只有先对设备进行配对，然后才能建立 RFCOMM 连接。在使用 Bluetooth API 发起加密连接时，系统会自动执行配对。*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(this.javaClass.simpleName, "缺少权限返回")
            return
        }

        //查询已连接的设备
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter!!.bondedDevices

        uuid = UUID.fromString(date.hashCode().toString())

        /**
         * startDiscovery()
         * cancelDiscovery()
         */
    }

    private fun connectBlueTooth(defaultName: String, mac: String) {

    }

    /**
     * 创建一个广播接收器，用于接收每台发现的设备的相关信息
     */
    private class BluetoothReceiver(val onListenerDevice: (String?, String?) -> Unit) : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device?.name
                val deviceHardwareAddress = device?.address
                onListenerDevice(deviceName, deviceHardwareAddress)
            }
        }
    }

    /**
     * 作为服务器连接
     */
    private inner class AcceptThread : Thread() {

       /* private val mmServiceSocket: BluetoothServerSocket by lazy(LazyThreadSafetyMode.NONE) {
            //bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord()
        }*/

        override fun run() {
            super.run()
        }
    }

    /**
     * 作为客户端连接
     */
    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        }

        override fun run() {
            bluetoothAdapter?.cancelDiscovery()
            mmSocket?.use {
                it.connect()
                manageMyConnectedSocket(it)
            }
        }
        fun cancel(){
            try {
                mmSocket?.close()
            }catch (e:Exception){
                Log.e(this.javaClass.simpleName, "作为客户端连接取消出错了")
            }
        }
    }

    /**
     * 连接管理
     */
    private fun manageMyConnectedSocket(socket: BluetoothSocket) {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}