package com.zhao.myapplication.bluetooth

import android.os.Handler

/**
 *创建时间： 2022/7/12
 *编   写：  zjf
 *页面功能: 一个定位置 在没有新的消息发送，6s后发送一次消息，位置这个连接
 */
class HeartbeatClient(private val handler: Handler) {

    private var isSend = false

    private val runnable = Runnable {
        if (!isSend) {
            handler.sendMessage(handler.obtainMessage(MessageState.SEND_HEARTBEAT))
        }
    }

    fun start() {
        handler.postDelayed(runnable, 6000)
    }

    private fun noticeSend() {
        if (!isSend) {
            isSend = true
        }
    }
}