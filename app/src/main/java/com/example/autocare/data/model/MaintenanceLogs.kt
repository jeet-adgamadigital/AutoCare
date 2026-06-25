package com.example.autocare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_logs",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["vehicleId"],
            childColumns = ["associateVehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MaintenanceLogs(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val associateVehicleId : Long,
    val type : String,
    val notes : String?,
    val createdAt : Long = System.currentTimeMillis(),
    val isCompleted : Boolean = false,
    val isSynced : Boolean = false
)
