package com.example.autocare.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    @SerialName("vehicle_id")
    val vehicleId: Long,
    @SerialName("user_id")
    val userId: String,
    @SerialName("vehicle_name")
    val vehicleName: String,
    @SerialName("registration_number")
    val registrationNumber: String
)