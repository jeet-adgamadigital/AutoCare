package com.example.autocare.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.model.VehicleEntity


@Database(
    entities = [VehicleEntity::class, MaintenanceLogs::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun dao() : AppDao
}