package com.zhao.myapplication.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 *创建时间： 2022/7/14
 *编   写：  zjf
 *页面功能:
 */
@SuppressLint("MissingPermission")

class ConnectService(private val context: Context,private val handler: Handler) {

    companion object {
        private const val STATE_NONE = 0
        private const val STATE_LISTEN = 1
        private const val STATE_CONNECTING = 2  // 启动传出连接
        private const val STATE_CONNECTED = 3   // 已经连接到远程设备

        private const val NAME_BLUETOOTH_SECURE = "bluetoothChatInsecure"
        private val uuid: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var serviceThread: ServiceThread? = null
    private var clientThread: ClientThread? = null

    private var connectedThread: ConnectedThread? = null

    private var mState: Int = 0

    init {
        val bluetoothManger: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManger.adapter

    }

    private fun mLog(msg: String) {
        Log.e(this::class.java.simpleName, msg)
    }



    fun initConnect() {

    }

    fun initListen() {
        if (serviceThread != null){
            serviceThread?.cancel()
            serviceThread = null
        }
        serviceThread = ServiceThread()
        serviceThread?.start()
    }

    fun initSend() {

    }

    /**
     * 主动连接设备
     * 开启connectThread
     */
    fun connect(device: BluetoothDevice) {
        if (mState == STATE_CONNECTING) {
            if (clientThread != null) {
                clientThread?.cancel()
                clientThread = null
            }
        }
        if (connectedThread != null) {
            connectedThread?.cancel()
            connectedThread = null
        }
        clientThread = ClientThread(device)
        clientThread?.start()
    }

    fun write(byteArray: ByteArray){
        connectedThread?.write(byteArray)
    }

    @Synchronized
    private fun connected(socket: BluetoothSocket, device: BluetoothDevice) {
/*        if (serviceThread != null) {
            serviceThread?.cancel()
            serviceThread = null
        }
        if (clientThread != null) {
            clientThread?.cancel()
            clientThread = null
        }
        if (connectedThread != null) {
            clientThread?.cancel()
            connectedThread = null
        }*/

        connectedThread = ConnectedThread(socket)
        connectedThread?.start()
        //val msg = "hello"
        //connectedThread?.write(msg.encodeToByteArray())
    }


    inner class ServiceThread : Thread() {
        private val serviceSocket: BluetoothServerSocket? by lazy {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(NAME_BLUETOOTH_SECURE, uuid)
        }

        init {
            if (serviceSocket != null) {
                mState = STATE_LISTEN
            }
        }


        override fun run() {
            mLog("service已启动")
            var socket: BluetoothSocket
            while (mState != STATE_CONNECTED) {
                try {
                    socket = serviceSocket!!.accept()
                } catch (e: Exception) {
                    mLog("阻塞中的连接异常")
                    //throw e
                    break
                }
                if (socket != null) {
                    mLog("查询到有连接进入")
                    connected(socket, socket.remoteDevice)
                    break
                    @Synchronized
                    when (mState) {
                        STATE_NONE -> {}
                        STATE_LISTEN -> {}
                        STATE_CONNECTING -> {
                            mLog("开始建立连接")
                            connected(socket, socket.remoteDevice)
                            break
                        }
                        STATE_CONNECTED -> {
                            try {
                                socket.close()
                            } catch (e: Exception) {
                                mLog("阻塞中的关闭异常")
                            }
                        }
                    }
                }
            }
        }

        fun cancel() {
            try {
                serviceSocket?.close()
            } catch (e: IOException) {
                mLog("关闭ServiceSocket异常")
            }
        }
    }

    inner class ClientThread(private val device: BluetoothDevice) : Thread() {

        private val socket: BluetoothSocket by lazy { device.createInsecureRfcommSocketToServiceRecord(uuid) }

        init {
            mState = STATE_CONNECTING
        }

        override fun run() {
            handler.sendMessage(handler.obtainMessage(BluetoothActivity.STOP_RECEIVER))
            try {
                mLog("开始连接")
                socket.connect()
            } catch (e: IOException) {
                try {
                    socket.close()
                } catch (e: Exception) {
                    mLog("取消socket异常")
                }
                return
            }
            mLog("开始建立连接")
            connected(socket, device)
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: Exception) {
                mLog("取消client socket异常")
            }
        }
    }

    inner class ConnectThread() : Thread() {

    }

    inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
        private var inputStream: InputStream? = null
        private var outputStream: OutputStream? = null

        init {
            inputStream = socket.inputStream
            outputStream = socket.outputStream
            mState = STATE_CONNECTED
            mLog(inputStream.toString())
            mLog(outputStream.toString())
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = inputStream!!.read(buffer)

                    // Send the obtained bytes to the UI Activity
                    mLog(buffer.decodeToString())
                } catch (e: IOException) {

                    break
                }
            }
        }

        fun write(buffer:ByteArray){
            try {
                outputStream?.write(buffer)

                // Share the sent message back to the UI Activity
                mLog("发送成功")
            } catch (e: IOException) {

                mLog("发送消息出错")
            }
        }

        fun cancel() {

        }
    }
}


