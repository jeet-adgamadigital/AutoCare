package com.example.autocare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val vehicleId : Long = 0,
    val vehicleName : String,
    val registrationNumber : String,
    val isSynced : Boolean
)
