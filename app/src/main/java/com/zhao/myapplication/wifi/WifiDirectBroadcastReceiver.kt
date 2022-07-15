package com.zhao.myapplication.wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Handler
import android.util.Log

/**
 *创建时间： 2022/7/12
 *编   写：  zjf
 *页面功能:
 */
@SuppressLint("MissingPermission")
class WifiDirectBroadcastReceiver (
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val context: Context,
    private val listener: (WifiP2pDeviceList) ->Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                when(intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1)){
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED ->{
                        Log.e("BroadcastReceiver","wifip2p已经启用")
                    }
                    else ->{
                        Log.e("BroadcastReceiver","wifip2p未启用")
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                manager.requestPeers(channel){  payers : WifiP2pDeviceList ->
                    if (payers.deviceList.isNotEmpty()){
                        Log.e("BroadcastReceiver","不为null，回调")
                        listener(payers)
                    }else{
                        Log.e("BroadcastReceiver","为null，结束")
                    }

                }
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }
        }
    }
}