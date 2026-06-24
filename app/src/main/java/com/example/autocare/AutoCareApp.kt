package com.example.autocare

import android.app.Application
import com.example.autocare.data.di.AppContainer

class AutoCareApp : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}