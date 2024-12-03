package com.example.productexplorer.utility.network

import android.app.Application
import com.example.productexplorer.utility.isInternetAvailable
import javax.inject.Inject

class NetworkStatus @Inject constructor(private val app: Application): INetworkStatus {
    override fun isConnected(): Boolean {
        return isInternetAvailable(app)
    }
}