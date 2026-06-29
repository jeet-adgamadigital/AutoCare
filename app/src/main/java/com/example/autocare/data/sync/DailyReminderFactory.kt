package com.example.autocare.data.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.autocare.R
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.remote.LogsRepository
import com.example.autocare.data.remote.VehicleRepository
import com.example.autocare.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DailyReminderFactory(
    private val context: Context,
    workerParams: WorkerParameters,
    private val logsRepository: LogsRepository,
    private val vehicleRepository: VehicleRepository,
    private val sessionManager: SessionManager
) : CoroutineWorker(context, workerParams){
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            val permission = sessionManager.isNotificationEnabled.first()
            if (permission){
                val pendingLogs = logsRepository.getPendingLogs()
                Log.d("Notification", "Pending Logs ${pendingLogs}")
                pendingLogs.forEach { logs ->
                    val vehicle = vehicleRepository.getVehicleById(logs.associateVehicleId)
                    sendNotification(logs, vehicle.vehicleName)
                    val completeLog = logs
                    completeLog.isCompleted = true
                    logsRepository.updateLog(completeLog)
                }
            }
            Result.success()
        } catch (e : Exception){
            Log.d("Notification", "Worker failed to execute")
            e.printStackTrace()
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(task : MaintenanceLogs, vehicleName : String) {
        Log.d("Notification", "Send Notification called $task")
        val channelId = "logs_reminder"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Maintenance Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Alerts for scheduled vehicle services due today"
            }
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(task.type)
            .setContentText("Service : ${task.type} for vehicle ${vehicleName} is due")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(task.id.hashCode(), notification)
    }


}