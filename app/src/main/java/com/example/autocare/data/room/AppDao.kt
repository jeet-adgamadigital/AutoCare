package com.example.autocare.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.autocare.data.model.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    //Vehicle Qeuries
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles() : Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE vehicleName LIKE '%' || :query || '%'")
    fun getSearchedVehicles(query : String) : Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle : VehicleEntity) : Long
}