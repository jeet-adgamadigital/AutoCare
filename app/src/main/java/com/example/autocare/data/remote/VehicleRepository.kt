package com.example.autocare.data.remote

import com.example.autocare.data.model.MaintenanceLogDto
import com.example.autocare.data.model.VehicleDto
import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.data.room.AppDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach

class VehicleRepository(
    private val dao : AppDao,
    private val supabaseClient: SupabaseClient
) {

    fun getAllVehicles(): Flow<List<VehicleEntity>>{
        return dao.getAllVehicles()
    }

    suspend fun getVehicleById(vehicleId : Long) : VehicleEntity{
        return dao.getVehicleById(vehicleId)
    }

    fun getSearchedVehicles(query : String) : Flow<List<VehicleEntity>>{
        return dao.getSearchedVehicles(query)
    }

    suspend fun insertVehicle(vehicle : VehicleEntity) : Result<Long>{
        return runCatching {
            dao.insertVehicle(vehicle)
        }
    }

    suspend fun updateVehicle(vehicle: VehicleEntity): Result<Int> {
        return runCatching {
            dao.updateVehicle(vehicle)
        }
    }

    suspend fun syncAllUnsyncedVehicles(){
        try {
            val unsyncedVehicle = dao.getUnsyncedVehicle()
            val currentUserId = supabaseClient.auth.currentUserOrNull()?.id
            unsyncedVehicle.forEach { vehicleEntity ->
                val payload = VehicleDto(
                    vehicleId = vehicleEntity.vehicleId,
                    userId = currentUserId ?: "",
                    vehicleName = vehicleEntity.vehicleName,
                    registrationNumber = vehicleEntity.registrationNumber
                )

                supabaseClient.from("vehicles").upsert(payload)
                val syncedVehicle = vehicleEntity
                vehicleEntity.isSynced = true
                dao.updateVehicle(syncedVehicle)
            }
        } catch (e : Exception){
            e.printStackTrace()
        }

    }

    suspend fun deleteVehicle(id : Long){
        dao.deleteVehicleById(id)
    }

    suspend fun deleteAllVehicle(){
        val vehicles = dao.getAllVehicles().first()
        vehicles.forEach { vehicleEntity ->
            dao.deleteVehicleById(vehicleEntity.vehicleId)
        }
    }
}