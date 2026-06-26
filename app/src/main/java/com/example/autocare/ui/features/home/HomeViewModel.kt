package com.example.autocare.ui.features.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.data.remote.LogsRepository
import com.example.autocare.data.remote.VehicleRepository
import com.example.autocare.data.sync.SyncWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val vehicleRepository: VehicleRepository,
    private val logsRepository: LogsRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow<HomeUiStates>(HomeUiStates.ListMode)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeNav>()
    val event = _event.asSharedFlow()

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery = _searchQuery.asStateFlow()

    // Vehicle Properties
    private val _vehicleName = MutableStateFlow<String>("")
    val vehicleName = _vehicleName.asStateFlow()

    private val _vehicleRegNumber = MutableStateFlow<String>("")
    val vehicleRegNumber = _vehicleRegNumber.asStateFlow()

    private val _currentVehicleId = MutableStateFlow<Long?>(null)
    var currentVehicleId = _currentVehicleId.asStateFlow()

    fun changeCurrentId(id: Long?){
        _currentVehicleId.value = id
    }
    val maintenanceLogs: StateFlow<List<MaintenanceLogs>> = _currentVehicleId
        .flatMapLatest { vehicleId ->
            if (vehicleId != null) {
                logsRepository.getLogs(vehicleId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val logs : StateFlow<List<MaintenanceLogs>> = logsRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())

    fun changeState(state : HomeUiStates){
        _uiState.value = state
    }



    //Vehicle Add Mode methods
    fun onVehicleNameChange(name : String){
        _vehicleName.value = name
    }

    fun onVehicleNumberChange(number : String){
        _vehicleRegNumber.value = number
    }
    fun clearVehicleInput(){
        _vehicleName.value = ""
        _vehicleRegNumber.value = ""
    }

    // Vehicle Edit Mode methods
    fun setVehicleInput(vehicle : VehicleEntity){
        _vehicleName.value = vehicle.vehicleName
        _vehicleRegNumber.value = vehicle.registrationNumber
    }

    fun insertVehicle(vehicleName : String, vehicleNumber : String, context: Context){
        changeState(HomeUiStates.Loading)
        viewModelScope.launch {
            val payload = VehicleEntity(
                vehicleName = vehicleName.trim(),
                registrationNumber = vehicleNumber.trim()
            )
            vehicleRepository.insertVehicle(payload)
                .onSuccess {
                    triggerAutomaticSync(context)
                    changeState(HomeUiStates.Success("Vehicle Added Successfully"))
                    delay(2000)
                    changeState(HomeUiStates.ListMode)
                }
                .onFailure {
                    changeState(HomeUiStates.Error("Could not enter a new vehicle"))
                }
        }
    }

    fun updateVehicle(vehicle: VehicleEntity, context: Context){
        changeState(HomeUiStates.Loading)
        viewModelScope.launch {
            val payload = VehicleEntity(
                vehicleId = vehicle.vehicleId,
                vehicleName = vehicleName.first(),
                registrationNumber = vehicleRegNumber.first(),
                isSynced = false
            )
            vehicleRepository.updateVehicle(payload)
                .onSuccess {
                    triggerAutomaticSync(context)
                    changeState(HomeUiStates.Success("Vehicle Added Successfully"))
                    delay(2000)
                    changeState(HomeUiStates.ListMode)
                }
                .onFailure {
                    changeState(HomeUiStates.Error("Could not enter a new vehicle"))
                }
        }
    }



    val itemsList: StateFlow<List<VehicleEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                vehicleRepository.getAllVehicles()
            } else {
                vehicleRepository.getSearchedVehicles(query.trim())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Log Properties
    private val _serviceType = MutableStateFlow<String>("")
    val serviceType = _serviceType.asStateFlow()

    private val _date = MutableStateFlow<String>("")
    val date = _date.asStateFlow()

    private val _notes = MutableStateFlow<String>("")
    val notes = _notes.asStateFlow()


    //Log methods
    fun onServiceTypeChange(service : String){
        _serviceType.value = service
    }

    fun onDateChange(date : String){
        _date.value = date
    }

    fun onNotesChange(notes : String){
        _notes.value = notes
    }

    fun insertLog(vehicleId: Long, service: String, date: Long, notes: String, context: Context) {
        changeState(HomeUiStates.Loading)
        viewModelScope.launch {
            try {
                val payload = MaintenanceLogs(
                    associateVehicleId = vehicleId,
                    type = service,
                    notes = notes,
                    date = date,
                )
                Log.d("log","${vehicleId}")
                logsRepository.insertLogs(payload)
                    .onSuccess {
                        triggerAutomaticSync(context)
                        changeState(HomeUiStates.Success("Log Added Successfully"))
                        delay(2000)
                        changeState(HomeUiStates.ListMode)
                    }
                    .onFailure {
                        Log.d("log","${payload.toString()}")
                        changeState(HomeUiStates.Error("Could not enter a new Log"))
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(HomeUiStates.Error("An unexpected error occurred"))
            }
            clearLogInput()
        }
    }

    fun updateLog(logId : Long,vehicleId : Long,type : String, notes : String, date : Long, isCompleted : Boolean,context: Context){
        changeState(HomeUiStates.Loading)
        viewModelScope.launch {
            val payload = MaintenanceLogs(
                id = logId,
                associateVehicleId = vehicleId,
                type = type,
                notes = notes,
                date = date,
                isCompleted = isCompleted,
                isSynced = false
            )
            logsRepository.updateLog(payload)
                .onSuccess {
                    triggerAutomaticSync(context)
                    changeState(HomeUiStates.Success("Log Updated Successfully"))
                    delay(2000)
                    changeState(HomeUiStates.ListMode)
                }
                .onFailure { throwable ->
                    changeState(HomeUiStates.Error("$throwable"))
                    Log.d("Log","$throwable")
                }
            clearLogInput()
        }
    }

    fun setLogInput(log : MaintenanceLogs){
        _serviceType.value = log.type
        _date.value = log.date.toString()
        _notes.value = log.notes?: ""
    }

    fun clearLogInput(){
        _serviceType.value = ""
        _date.value = ""
        _notes.value = ""
    }

    // Sync Trigger function
    private fun triggerAutomaticSync(context : Context) {
        val syncConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncWork = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(syncConstraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "supabase_sync",
            ExistingWorkPolicy.REPLACE,
            syncWork
        )
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application, private val vehicleRepository: VehicleRepository, private val logsRepository: LogsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application, vehicleRepository,logsRepository) as T
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
        data class VehicleEditMode(val vehicle : VehicleEntity) : HomeUiStates
        data class AddLogsMode(val vehicleId : Long) : HomeUiStates
        data class EditLogsMode(val log : MaintenanceLogs) : HomeUiStates
        data class Success(val message : String) : HomeUiStates
        data class Error(val message : String) : HomeUiStates
        object Loading : HomeUiStates
    }

    sealed interface HomeNav{
        object NavigateToSettings : HomeNav
    }
}