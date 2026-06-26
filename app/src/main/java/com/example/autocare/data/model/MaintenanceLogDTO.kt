package com.example.autocare.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceLogDto(
    @SerialName("id")
    val id: Long,
    @SerialName("associate_vehicle_id")
    val associateVehicleId: Long,
    @SerialName("type")
    val type: String,
    @SerialName("notes")
    val notes: String?,
    @SerialName("date")
    val date: Long,
    @SerialName("is_completed")
    val isCompleted: Boolean
)
