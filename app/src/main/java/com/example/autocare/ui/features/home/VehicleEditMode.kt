package com.example.autocare.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autocare.data.model.VehicleEntity
import com.example.autocare.ui.theme.*

@Composable
fun VehicleEditMode(
    vehicle : VehicleEntity,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val vehicleName by viewModel.vehicleName.collectAsStateWithLifecycle()
    val vehicleNumber by viewModel.vehicleRegNumber.collectAsStateWithLifecycle()
    val maintenanceLogs by viewModel.maintenanceLogs.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlueBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .clickable { viewModel.changeState(HomeViewModel.HomeUiStates.ListMode) }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Edit Vehicle & Logs",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        }
        Text(
            text = "Modify vehicle characteristics and view full histories of diagnostic tune-ups below.",
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            lineHeight = 20.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(BackgroundGray, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "VEHICLE SPEC/MODEL NAME",
                            color = LightSubtext,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(
                            value = vehicleName,
                            onValueChange = { viewModel.onVehicleNameChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = LightBlueField,
                                unfocusedContainerColor = LightBlueField,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "VEHICLE REGISTRATION NUMBER / PLATE",
                            color = LightSubtext,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(
                            value = vehicleNumber,
                            onValueChange = { viewModel.onVehicleNumberChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = LightBlueField,
                                unfocusedContainerColor = LightBlueField,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Maintenance History",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlueCard
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                if (maintenanceLogs.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No maintenance logs reported for this vehicle.",
                                color = TextGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(
                        items = maintenanceLogs
                    ) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = log.type,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkBlueCard,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = log.date.toString(),
                                        color = TextGray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                if (!log.notes.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = log.notes,
                                        color = TextGray,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Button(
                onClick = {
                    if (vehicleName.isNotBlank() && vehicleNumber.isNotBlank()) {
                        viewModel.updateVehicle(vehicle)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlueCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Settings & Specs", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}