package com.example.productexplorer.utility.network

import android.app.Application
import com.example.productexplorer.utility.isInternetAvailable

class NetworkStatus(val app:Application): INetworkStatus {
    override fun isConnected(): Boolean {
        return isInternetAvailable(app)
    }
}