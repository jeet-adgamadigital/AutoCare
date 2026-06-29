package com.example.autocare

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.autocare.data.di.AppContainer
import com.example.autocare.data.sync.CustomWorkerFactory
import com.example.autocare.data.sync.DailyReminderFactory
import java.util.concurrent.TimeUnit

class AutoCareApp : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        val vehiclesRepo = (applicationContext as AutoCareApp).container.vehicleRepository
        val logsRepo = (applicationContext as AutoCareApp).container.logsRepository
        val sessionManager = (applicationContext as AutoCareApp).container.sessionManager

        val customConfiguration = Configuration.Builder()
            .setWorkerFactory(CustomWorkerFactory(
                vehiclesRepo, logsRepo, sessionManager
            ))
            .build()

        WorkManager.initialize(this, customConfiguration)

        scheduleDailyReminder()
    }

    private fun scheduleDailyReminder() {
        Log.d("Notification", "Cron Job called")
        val dailyReminderRequest = PeriodicWorkRequestBuilder<DailyReminderFactory>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_maintenance_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyReminderRequest
        )
    }
}