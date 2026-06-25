package com.example.autocare.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autocare.data.model.VehicleEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMode(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val vehiclesList by viewModel.itemsList.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F6F8),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.changeState(HomeViewModel.HomeUiStates.AddVehicleMode)
                },
                containerColor = Color(0xFF192A46),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vehicle")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Search vehicles...", color = Color(0xFFBACAD6)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color(0xFF8A99AD)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "My Vehicles (${vehiclesList.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF192A46)
            )

            Spacer(modifier = Modifier.height(12.dp))
            if (vehiclesList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "No vehicles found. Add your first vehicle!" else "No matching results found.",
                        color = Color(0xFF8A99AD),
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = vehiclesList,
                        key = { vehicle -> vehicle.vehicleId }
                    ) { vehicle ->
                        VehicleItemCard(
                            vehicle = vehicle,
                            onCardClick = {
                                viewModel.changeState(HomeViewModel.HomeUiStates.AddLogsMode(vehicle.vehicleId))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VehicleItemCard(
    vehicle: VehicleEntity,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehicle.vehicleName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF192A46)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vehicle.registrationNumber.uppercase(),
                    fontSize = 13.sp,
                    color = Color(0xFF8A99AD),
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "❯",
                color = Color(0xFFBACAD6),
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}