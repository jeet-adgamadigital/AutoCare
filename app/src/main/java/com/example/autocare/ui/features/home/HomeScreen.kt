package com.example.autocare.ui.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToSettings : () -> Unit,
    factory: HomeViewModel.Factory
){

    val viewModel : HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when(event) {
                HomeViewModel.HomeNav.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    when(val state = uiState) {
        HomeViewModel.HomeUiStates.ListMode -> {
            ListMode(
                viewModel = viewModel
            )
        }
        is HomeViewModel.HomeUiStates.AddLogsMode -> TODO()
        is HomeViewModel.HomeUiStates.EditLogsMode -> TODO()
        is HomeViewModel.HomeUiStates.Error -> TODO()
        is HomeViewModel.HomeUiStates.Success -> TODO()
        is HomeViewModel.HomeUiStates.VehicleEditMode -> TODO()
        HomeViewModel.HomeUiStates.AddVehicleMode -> TODO()
    }


}