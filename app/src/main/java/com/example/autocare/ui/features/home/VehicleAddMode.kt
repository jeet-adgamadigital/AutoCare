package com.example.autocare.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autocare.ui.theme.*

@Composable
fun VehicleAddMode(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val vehicleName by viewModel.vehicleName.collectAsStateWithLifecycle()
    val vehicleRegNumber by viewModel.vehicleRegNumber.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlueBackground)
    ) {
        // App Bar Title Block
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
                text = "Add Vehicle Spec",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        }

        // Context Subtitle Description Text
        Text(
            text = "Input tracking properties to monitor oil levels, fuel efficiency metrics, and component modifications.",
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            lineHeight = 20.sp
        )

        // Lower Rounded Panel containing Form Inputs and bottom-docked Action Button
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
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
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
                        placeholder = { Text("e.g. Ford Mustang GT", color = TextGray) },
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
                        value = vehicleRegNumber,
                        onValueChange = { viewModel.onVehicleNumberChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = LightBlueField,
                            unfocusedContainerColor = LightBlueField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("e.g. DL-3C-AA-1111", color = TextGray) },
                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        if (vehicleName.isNotBlank() && vehicleRegNumber.isNotBlank()) {
                            viewModel.insertVehicle(vehicleName, vehicleRegNumber, context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Text("Confirm & Save Vehicle", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}