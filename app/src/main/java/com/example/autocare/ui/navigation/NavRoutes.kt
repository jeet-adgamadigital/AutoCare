package com.example.autocare.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoutes {

    @Serializable
    object Splash : NavRoutes

    @Serializable
    object Auth : NavRoutes

    @Serializable
    object Home : NavRoutes

    @Serializable
    object Settings : NavRoutes
}