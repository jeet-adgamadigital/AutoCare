package com.example.autocare.ui.features.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.remote.AuthRepository
import com.example.autocare.data.remote.VehicleRepository
import com.example.autocare.data.session.SessionManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.jvm.java

class SettingsScreenViewModel (
    private val vehicleRepository: VehicleRepository,
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository
) : ViewModel(){

    private val _event = MutableSharedFlow<SettingsNav>()
    val event = _event.asSharedFlow()

    val isNotificationsEnabled: StateFlow<Boolean> = sessionManager.isNotificationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun logOut(){
        viewModelScope.launch {
            sessionManager.setLoginStatus(false)
            authRepository.signOut()
            _event.emit(SettingsNav.NavigateToSignUp)
        }
    }

    fun deleteCache(){
        viewModelScope.launch {
            vehicleRepository.deleteAllVehicle()
        }
    }



    fun changeNotificationStatus(status : Boolean){
        viewModelScope.launch {
            sessionManager.setNotificationStatus(status)
        }
    }

    class Factory(
        private val sessionManager: SessionManager,
        private val vehicleRepository: VehicleRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsScreenViewModel::class.java)) {
                return SettingsScreenViewModel(
                    vehicleRepository = vehicleRepository,
                    sessionManager = sessionManager,
                    authRepository = authRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class context requested")
        }
    }

    sealed interface SettingsNav{
        object NavigateToSignUp : SettingsNav
        object NavigateToHome : SettingsNav
    }
}