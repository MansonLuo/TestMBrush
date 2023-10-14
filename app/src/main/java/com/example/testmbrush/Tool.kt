package com.example.testmbrush

import android.content.Context
import android.net.wifi.WifiManager

class Tool(private val context: Context) {
    val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    fun getConnectWifiSsid(): String {
        val wifiInfo = wifiManager.connectionInfo
        val wifiInfo1 = wifiInfo.ssid.replace("\"", "").replace("\"", "")

        return wifiInfo1
    }
}