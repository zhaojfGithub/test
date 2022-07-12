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
import android.os.*
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.zhao.myapplication.databinding.ActivityBluetoothBinding
import java.util.*

class BluetoothActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBluetoothBinding
    private lateinit var permissionLaunch: ActivityResultLauncher<String>
    private lateinit var bluetoothLaunch: ActivityResultLauncher<String>
    private lateinit var receiver: BluetoothReceiver
    private lateinit var date: Date
    private var bluetoothAdapter: BluetoothAdapter? = null

    private lateinit var uuid: UUID


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        receiver = BluetoothReceiver {

        }
        registerReceiver(receiver, filter)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionLaunch.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            settingBluetooth()
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun settingBluetooth() {
        val bluetoothManger: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManger.adapter
        if (bluetoothAdapter == null) {
            Log.e(this.javaClass.simpleName, "不支持蓝牙")
            return
        }
        if (!bluetoothAdapter!!.isEnabled) {
            bluetoothLaunch.launch(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            return
        }
        /*被配对是指两台设备知晓彼此的存在，具有可用于身份验证的共享链路密钥，并且能够与彼此建立加密连接。
        被连接是指设备当前共享一个 RFCOMM 通道，并且能够向彼此传输数据。当前的 Android Bluetooth API 要求规定，
        只有先对设备进行配对，然后才能建立 RFCOMM 连接。在使用 Bluetooth API 发起加密连接时，系统会自动执行配对。*/
/*        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(this.javaClass.simpleName, "缺少权限返回")
            permissionLaunch.launch(Manifest.permission.BLUETOOTH_CONNECT)
            return
        }*/

        //查询已连接的设备
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter!!.bondedDevices
        pairedDevices?.forEach {
            Log.e(this.javaClass.simpleName,it.toString())
        } ?:run{
            Log.e(this.javaClass.simpleName,"查询链接没有设备")
        }
        uuid = UUID.randomUUID()

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
    private class BluetoothReceiver(val onListenerDevice: (BluetoothDevice) -> Unit) : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null){
                    onListenerDevice(device)
                }
            }
        }
    }


    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun showListDialog(){

    }
}