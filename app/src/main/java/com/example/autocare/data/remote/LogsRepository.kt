package com.example.autocare.data.remote

import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.room.AppDao
import kotlinx.coroutines.flow.Flow

class LogsRepository(
    private val dao: AppDao
) {

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
}