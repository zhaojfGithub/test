package com.zhao.myapplication.wifi

import android.annotation.SuppressLint
import android.app.Service
import android.content.*
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull
import com.zhao.myapplication.databinding.ActivityWifiBinding
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

/**
 * wifi
 * 1.扫描
 *   扫描过程分三步
 *   1.为SCAN_RESULTS_AVAILABLE注册一个广播接收器
 *   2.请求扫描
 *   3.获取扫描结果
 */
class WifiActivity : AppCompatActivity(), WifiP2pManager.ConnectionInfoListener {

    private lateinit var activityWifiBinding: ActivityWifiBinding

    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }
    var mChannel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    var intentFilter: IntentFilter? = null
    var info: WifiP2pInfo? = null
    var clientService : ClientService? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWifiBinding = ActivityWifiBinding.inflate(layoutInflater)
        createWifiP2p()

    }

    /**
     * wifi直连p2p
     */
    private fun createWifiP2p() {
        mChannel = manager?.initialize(this, mainLooper, null)
        mChannel?.also { channel ->
            receiver = WifiDirectBroadcastReceiver(manager = manager!!, channel = channel, context = this) {
                mLog("发现wifi对等设备")
                it.deviceList.forEach { device ->
                    mLog(device.deviceName)
                }
            }
        }
        intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
    }

    /**
     * 启用发现对等设备
     */
    @SuppressLint("MissingPermission")
    private fun deviceDiscovery() {
        manager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                mLog("发现打印设备成功")
            }

            override fun onFailure(p0: Int) {
                mLog("发现打印设备遇到了问题p0 = $p0")
            }
        })
    }

    /**
     * 连接设备
     */
    @SuppressLint("MissingPermission")
    private fun connectDevice(device: WifiP2pDevice) {
        val config = WifiP2pConfig()
        //mac
        config.deviceAddress = device.deviceAddress
        mChannel?.also {
            manager?.connect(it, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    mLog("连接设备成功")
                }

                override fun onFailure(p0: Int) {
                    mLog("连接打印设备遇到了问题p0 = $p0")
                }
            })
        }
    }

    /**
     * 开启客户端服务
     */
    private fun startClientService() {
        val serviceIntent = Intent(this, ClientService::class.java).apply {
            setAction(ClientService.ACTION_SEND_FILE)
            //putExtra(ClientService.EXTRAS_FILE_PATH,u)
            putExtra(ClientService.EXTRAS_GROUP_OWNER_ADDRESS, info?.groupOwnerAddress?.hostAddress)
            putExtra(ClientService.EXTRAS_GROUP_OWNER_PORT, 8988)
        }
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE)
    }

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            clientService = (service as ClientService.ClientBinder).service
            clientService?.setData(info?.groupOwnerAddress?.hostAddress,8988)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
        this.info = p0
    }


    override fun onResume() {
        super.onResume()
        receiver?.also {
            registerReceiver(receiver, intentFilter)
            deviceDiscovery()
        }
    }

    override fun onPause() {
        super.onPause()
        receiver?.also {
            unregisterReceiver(it)
        }
    }

    private fun mLog(msg: String) {
        Log.e(this::class.java.simpleName, msg)
    }


    private class ServiceCommunicationThread : Thread() {
        private val serviceSocket = ServerSocket()
        private val client = serviceSocket.accept()
        private val inputStream = client.getInputStream()
        private val outputStream = client.getOutputStream()
        private val mmBuffer: ByteArray = ByteArray(1024)
        override fun run() {
            while (true) {
                try {
                    outputStream.write(mmBuffer)
                } catch (e: Exception) {
                    Log.e("CommunicationThread", "接受数据出现了问题")
                }
            }
        }

        fun write(bytes: ByteArray) {
            try {
                inputStream.read(bytes)
            } catch (e: IOException) {
                Log.e("CommunicationThread", "发送数据出现了io异常")
            } catch (e: Exception) {
                Log.e("CommunicationThread", "发送数据出现了其他异常")
            }
        }

        fun close() {
            outputStream.close()
            inputStream.close()
            client.close()
            serviceSocket.close()
        }
    }

    class ClientService : Service() {

        companion object {
            const val SOCKET_TIMEOUT = 5000
            const val ACTION_SEND_FILE = "com.zhao.myapplication.SEND_FILE"
            const val EXTRAS_FILE_PATH = "file_url"
            const val EXTRAS_GROUP_OWNER_ADDRESS = "go_host"
            const val EXTRAS_GROUP_OWNER_PORT = "go_port"
        }

        private var socket : Socket? =null

        override fun onBind(intent: Intent?): IBinder {
            return ClientBinder()
        }

        override fun onUnbind(intent: Intent?): Boolean {
            return super.onUnbind(intent)
        }

        fun setData(hostAddress: String?, port: Int) {
            if (hostAddress == null) return
            if (socket != null){
                socket!!.close()
            }
            socket = Socket()

            try {
                socket?.apply {
                    bind(null)
                    connect(InetSocketAddress(hostAddress,port), SOCKET_TIMEOUT)
                    val inputStream = this.getInputStream()
                    val outputStream = this.getOutputStream()
                    val cr = applicationContext.contentResolver

                }

            }catch (e:Exception){

            }
        }

        inner class ClientBinder : Binder() {
            val service: ClientService
                get() = this@ClientService
        }

    }


}