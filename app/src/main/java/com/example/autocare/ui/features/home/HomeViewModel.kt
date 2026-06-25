package com.example.autocare.ui.features.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.data.remote.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    application: Application,
    private val appRepository: AppRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow<HomeUiStates>(HomeUiStates.ListMode)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeNav>()
    val event = _event.asSharedFlow()

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery = _searchQuery.asStateFlow()

    val itemsList: StateFlow<List<VehicleEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                appRepository.getAllVehicles()
            } else {
                appRepository.getSearchedVehicles(query.trim())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun changeState(state : HomeUiStates){
        _uiState.value = state
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application, private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application, appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel requested: ${modelClass.name}")
        }
    }

    fun onQueryChange(query : String){
        _searchQuery.value = query
    }



    sealed interface HomeUiStates{
        object ListMode : HomeUiStates
        object AddVehicleMode : HomeUiStates
        data class VehicleEditMode(val vehicleId : Long) : HomeUiStates
        data class AddLogsMode(val vehicleId : Long) : HomeUiStates
        data class EditLogsMode(val logId : Long) : HomeUiStates
        data class Success(val message : String) : HomeUiStates
        data class Error(val message : String) : HomeUiStates
        object Loading
    }

    sealed interface HomeNav{
        object NavigateToSettings : HomeNav
    }
}