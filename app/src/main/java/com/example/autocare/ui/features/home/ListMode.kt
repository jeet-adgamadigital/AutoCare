package com.example.autocare.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autocare.R
import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.ui.theme.BackgroundGray
import com.example.autocare.ui.theme.DarkBlueBackground
import com.example.autocare.ui.theme.DarkBlueCard
import com.example.autocare.ui.theme.LightSubtext
import com.example.autocare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMode(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val vehiclesList by viewModel.itemsList.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlueBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "AutoCare Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AutoCare",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "VEHICLE LOG MANAGER",
                        color = LightSubtext,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BackgroundGray, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onQueryChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = { Text("Search vehicles...", color = TextGray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = TextGray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "My Vehicles (${vehiclesList.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueCard
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (vehiclesList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isBlank()) "No vehicles found. Add your first vehicle!" else "No matching results found.",
                                color = TextGray,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
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
                                        viewModel.changeCurrentId(vehicle.vehicleId)
                                        viewModel.setVehicleInput(vehicle)
                                        viewModel.changeState(HomeViewModel.HomeUiStates.VehicleEditMode(vehicle))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.changeState(HomeViewModel.HomeUiStates.AddVehicleMode) },
            containerColor = DarkBlueCard,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(bottom = 16.dp, end = 24.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vehicle")
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
                    color = DarkBlueCard
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vehicle.registrationNumber.uppercase(),
                    fontSize = 13.sp,
                    color = TextGray,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "❯",
                color = TextGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}