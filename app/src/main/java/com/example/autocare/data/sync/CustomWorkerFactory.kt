package com.example.autocare.data.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.autocare.data.remote.LogsRepository
import com.example.autocare.data.remote.VehicleRepository
import com.example.autocare.data.session.SessionManager
import io.github.jan.supabase.SupabaseClient

class CustomWorkerFactory(
    private val vehiclesRepository: VehicleRepository,
    private val logsRepository: LogsRepository,
    private val sessionManager: SessionManager
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> {
                SyncWorker(
                    appContext,
                    workerParameters,
                    logsRepository,
                    vehiclesRepository
                )
            }

            DailyReminderFactory::class.java.name -> {
                DailyReminderFactory(
                    appContext,
                    workerParameters,
                    logsRepository,
                    vehiclesRepository,
                    sessionManager = sessionManager
                )
            }

            else -> null
        }
    }
}