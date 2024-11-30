package com.example.productexplorer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProductApplication: Application() {

    override fun onCreate() {
        super.onCreate()

    }
}