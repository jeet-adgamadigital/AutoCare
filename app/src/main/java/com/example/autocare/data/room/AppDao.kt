package com.example.autocare.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.model.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    //Vehicle Queries
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles() : Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE vehicleId = :vehicleId")
    suspend fun getVehicleById(vehicleId : Long) : VehicleEntity

    @Query("SELECT * FROM vehicles WHERE vehicleName LIKE '%' || :query || '%'")
    fun getSearchedVehicles(query : String) : Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle : VehicleEntity) : Long

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity) : Int


    //Logs Queries
    @Query("SELECT * FROM maintenance_logs WHERE associateVehicleId = :vehicleId")
    fun getLogsByVehicleId(vehicleId : Long) : Flow<List<MaintenanceLogs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log : MaintenanceLogs) : Long
}