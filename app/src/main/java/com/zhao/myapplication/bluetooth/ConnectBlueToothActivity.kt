package com.zhao.myapplication.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhao.myapplication.R
import com.zhao.myapplication.databinding.ActivityConnectBluetoothBinding

class ConnectBlueToothActivity : AppCompatActivity() {

    companion object {
        private const val BLUETOOTH_ADDRESS = "address"

        fun start(context: Activity, address: String) {
            val intent = Intent(context, ConnectBlueToothActivity::class.java)
            intent.putExtra(BLUETOOTH_ADDRESS, address)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityConnectBluetoothBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initConnect()
    }

    private fun initView() {

    }

    private fun initConnect() {
        val address = intent.getStringExtra(BLUETOOTH_ADDRESS) ?: return
        val bluetoothManger: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManger.adapter
        val bluetoothDevice: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
    }
}