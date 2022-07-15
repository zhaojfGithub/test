package com.zhao.myapplication.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.zhao.myapplication.R
import com.zhao.myapplication.databinding.ActivityBluetoothBinding
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothActivity : AppCompatActivity() {

    companion object {
        const val STOP_RECEIVER = 3
    }

    private lateinit var binding: ActivityBluetoothBinding
    private lateinit var permissionLaunch: ActivityResultLauncher<String>
    private lateinit var bluetoothLaunch: ActivityResultLauncher<String>

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var connectService : ConnectService? =null
    private var receiver: BluetoothReceiver? = null
    private lateinit var uuid: UUID

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                STOP_RECEIVER -> {
                    unregisterNonReceiver()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bluetoothManger: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManger.adapter
        if (bluetoothAdapter == null) {
            showHintDialog()
            return
        }
        connectService = ConnectService(this,handler)
        connectService!!.initListen()
        initView()
        initLaunch()
    }

    private fun initLaunch() {
        //请求单个权限
        permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }
        //匿名内部类  打开蓝牙
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

        }
    }

    private fun initView() {
        binding.apply {
            listView.adapter = BluetoothListAdapter(this@BluetoothActivity, R.layout.item_bluetooth, arrayListOf())
            listView.setOnItemClickListener { _, _, position, _ ->
                val device = (listView.adapter as BluetoothListAdapter).getItem(position)
                if (device == null) {
                    showToast("bluetooth device is null?")
                } else {
                    connectBlueTooth(device)
                }
            }
            button1.setOnClickListener {
                permissionLaunch.launch(Manifest.permission.BLUETOOTH)
            }
            button2.setOnClickListener {
                if (isPermission()) {
                    showToast("请先申请权限")
                    return@setOnClickListener
                }
                bluetoothLaunch.launch(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            }
            button3.setOnClickListener {
                queryBluetoothDevice()
            }
            button4.setOnClickListener {
                val msg = "hello"
                connectService?.write(msg.encodeToByteArray())
                //searchBluetoothDevice()
            }
        }
    }

    private fun isPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun queryBluetoothDevice() {
        val bluetoothManger: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManger.adapter
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter!!.bondedDevices
        pairedDevices?.forEach {
            Log.e(this.javaClass.simpleName, it.name + it.toString())
        } ?: run {
            Log.e(this.javaClass.simpleName, "查询链接没有设备")
        }
        if (pairedDevices == null || pairedDevices.isEmpty()) {
            showToast("没有已经配对的设备")
        } else {
            (binding.listView.adapter as BluetoothListAdapter).also { adapter ->
                adapter.clear()
                adapter.addAll(pairedDevices.toList())
            }
        }
    }

    private fun searchBluetoothDevice() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        receiver = BluetoothReceiver { device ->
            binding.listView.also { listView ->
                (listView.adapter as BluetoothListAdapter).add(device)
            }
        }
        registerReceiver(receiver, filter)
        val message = handler.obtainMessage(STOP_RECEIVER)
        handler.sendMessageAtTime(message, 8 * 1000)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun connectBlueTooth(bluetoothDevice: BluetoothDevice?) {
        if (bluetoothDevice == null) return
        connectService!!.connect(bluetoothDevice)
    }

    override fun onStop() {
        super.onStop()
        unregisterNonReceiver()
    }

    private fun unregisterNonReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
    }

    private fun showHintDialog(msg: String = "该设备不支持蓝牙") {
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
                finish()
            }.show()
    }


    private class BluetoothListAdapter(context: Context, private val resourceId: Int, list: List<BluetoothDevice>) :
        ArrayAdapter<BluetoothDevice>(context, resourceId, list) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)
            val name = view.findViewById<TextView>(R.id.name)
            val mac = view.findViewById<TextView>(R.id.mac)
            val bluetoothDevice = getItem(position) ?: return view
            name.text = bluetoothDevice.name
            mac.text = bluetoothDevice.address
            return view
        }
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
                if (device != null) {
                    onListenerDevice(device)
                }
            }
        }
    }

}
