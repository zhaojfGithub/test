package com.zhao.myapplication.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 *创建时间： 2022/7/12
 *编   写：  zjf
 *页面功能: 管理之间通话的线程
 */
class ConnectBluetoothThread(private val handler: Handler,private val mmSocket: BluetoothSocket) : Thread() {
    private val mmInStream: InputStream = mmSocket.inputStream
    private val mmOutStream: OutputStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(1024)

    override fun run() {
        var numBytes: Int
        while (true) {
            // Read from the InputStream.
            numBytes = try {
                mmInStream.read(mmBuffer)
            } catch (e: IOException) {

                break
            }

            // Send the obtained bytes to the UI activity.

        }
    }

    fun write(bytes: ByteArray) {
        try {
            mmOutStream.write(bytes)
        } catch (e: IOException) {
            //Log.e(TAG, "Error occurred when sending data", e)

            // Send a failure message back to the activity.
            /*val writeErrorMsg = handler.obtainMessage(BluetoothActivity.MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", "Couldn't send data to the other device")
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)*/
            return
        }

        // Share the sent message with the UI activity.
        /*val writtenMsg = handler.obtainMessage(
            BluetoothActivity.MESSAGE_WRITE, -1, -1, mmBuffer
        )
        writtenMsg.sendToTarget()*/

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                //Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}