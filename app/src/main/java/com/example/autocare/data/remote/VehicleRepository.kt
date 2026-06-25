package com.example.autocare.data.remote

import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.data.room.AppDao
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val dao : AppDao
) {

    fun getAllVehicles(): Flow<List<VehicleEntity>>{
        return dao.getAllVehicles()
    }

    fun getSearchedVehicles(query : String) : Flow<List<VehicleEntity>>{
        return dao.getSearchedVehicles(query)
    }

    suspend fun insertVehicle(vehicle : VehicleEntity) : Result<Long>{
        return runCatching {
            dao.insertVehicle(vehicle)
        }
    }
}