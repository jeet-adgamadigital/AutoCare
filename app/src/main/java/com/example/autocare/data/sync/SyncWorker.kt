package com.example.autocare.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.autocare.data.remote.LogsRepository
import com.example.autocare.data.remote.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context : Context,
    workerParameter : WorkerParameters,
    private val logsRepository: LogsRepository,
    private val vehicleRepository: VehicleRepository
) : CoroutineWorker(context, workerParameter){
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("SyncWorker", "Repository-backed sync pipeline triggered.")

        try {
            vehicleRepository.syncAllUnsyncedVehicles()
            logsRepository.syncAllUnsyncedLogs()
            Log.d("SyncWorker", "All repositories executed sync cleanly.")
            Result.success()

        } catch (e : Exception){
            Log.e("SyncWorker", "Sync transaction encountered a failure error.", e)
            Result.retry()
        }
    }


}