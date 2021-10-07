package com.amp.bemobile.view.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class ConnectionDataManager(private val context: Context) {

    fun hasInternet(): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?)?.run {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    val cap = this.getNetworkCapabilities(this.activeNetwork)
                    cap?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    this.allNetworks.any { network ->
                        val nInfo = this.getNetworkInfo(network)
                        nInfo != null && nInfo.isConnected
                    }
                }
                else -> false
            }
        } ?: false
}
