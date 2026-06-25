package com.example.autocare.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autocare.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    factory: HomeViewModel.Factory
) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                HomeViewModel.HomeNav.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlueBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val state = uiState) {
                HomeViewModel.HomeUiStates.ListMode -> {
                    ListMode(viewModel = viewModel)
                }
                HomeViewModel.HomeUiStates.AddVehicleMode -> {
                    VehicleAddMode(viewModel = viewModel)
                }
                is HomeViewModel.HomeUiStates.Success -> {
                    SuccessScreen(
                        message = state.message,
                        onTimeout = {
                            viewModel.changeState(HomeViewModel.HomeUiStates.ListMode)
                        }
                    )
                }
                is HomeViewModel.HomeUiStates.AddLogsMode -> {
                    val vehicleId = state.vehicleId
                    AddLogsMode(
                        vehicleId = vehicleId,
                        viewModel = viewModel
                    )
                }
                is HomeViewModel.HomeUiStates.EditLogsMode -> TODO()
                is HomeViewModel.HomeUiStates.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onDismiss = {
                            viewModel.changeState(HomeViewModel.HomeUiStates.ListMode)
                        }
                    )
                }
                is HomeViewModel.HomeUiStates.VehicleEditMode -> {
                    val vehicle = state.vehicle
                    VehicleEditMode(
                        vehicle,
                        viewModel
                    )
                }
                HomeViewModel.HomeUiStates.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = OrangeAccent)
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessScreen(
    message: String,
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlueBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(color = OrangeAccent.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = OrangeAccent,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Success!",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = message,
                color = Color(0xFFBACAD6),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlueBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(color = Color(0xFFEF5350).copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    color = Color(0xFFEF5350),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Error Occurred",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = message,
                color = Color(0xFFBACAD6),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Tap here to return to dashboard",
                color = OrangeAccent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(8.dp)
            )
        }
    }
}