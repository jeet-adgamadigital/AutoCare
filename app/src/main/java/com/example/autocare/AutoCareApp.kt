package com.example.autocare

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.autocare.data.di.AppContainer
import com.example.autocare.data.sync.CustomWorkerFactory

class AutoCareApp : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        val vehiclesRepo = (applicationContext as AutoCareApp).container.vehicleRepository
        val logsRepo = (applicationContext as AutoCareApp).container.logsRepository
        val supabase = (applicationContext as AutoCareApp).container.supabaseClient

        val customConfiguration = Configuration.Builder()
            .setWorkerFactory(CustomWorkerFactory(vehiclesRepo, logsRepo, supabase))
            .build()


        WorkManager.initialize(this, customConfiguration)
    }
}