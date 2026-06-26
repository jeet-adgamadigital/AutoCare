package com.example.autocare.data.remote

import com.example.autocare.data.model.MaintenanceLogDto
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.room.AppDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow

class LogsRepository(
    private val dao: AppDao,
    private val supabaseClient: SupabaseClient
) {

    fun getAll() : Flow<List<MaintenanceLogs>> {
        return dao.getAllLogs()
    }

    fun getLogs(vehicleId : Long) : Flow<List<MaintenanceLogs>> {
        return dao.getLogsByVehicleId(vehicleId)
    }

    suspend fun insertLogs(log : MaintenanceLogs) : Result<Long> {
        return runCatching {
            dao.insertLog(log)
        }
    }

    suspend fun updateLog(log : MaintenanceLogs) : Result<Int> {
        return runCatching {
            dao.updateLogs(log)
        }
    }

    suspend fun syncAllUnsyncedLogs(){
        try {
            val unsyncedLogs = dao.getUnsyncedLogs()
            unsyncedLogs.forEach { log ->
                val payload = MaintenanceLogDto(
                    log.id,
                    log.associateVehicleId,
                    log.type,
                    log.notes,
                    log.date,
                    log.isCompleted
                )

                supabaseClient.from("maintenance_logs").upsert(payload)
                val syncedLog = log
                syncedLog.isSynced = true
                dao.updateLogs(syncedLog)
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }
}