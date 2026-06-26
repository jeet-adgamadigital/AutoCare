package com.example.autocare.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autocare.data.model.MaintenanceLogs
import com.example.autocare.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLogsMode(
    log: MaintenanceLogs,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val serviceType by viewModel.serviceType.collectAsStateWithLifecycle()
    val rawDateText by viewModel.date.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTimestamp by remember { mutableStateOf<Long?>(log.date) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = log.date)

    LaunchedEffect(key1 = log.id) {
        viewModel.changeCurrentId(log.associateVehicleId)
        viewModel.onServiceTypeChange(log.type)
        viewModel.onNotesChange(log.notes ?: "")

        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        viewModel.onDateChange(formatter.format(Date(log.date)))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            selectedTimestamp = selectedMillis
                            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            val dateString = formatter.format(Date(selectedMillis))
                            viewModel.onDateChange(dateString)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = DarkBlueCard, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = TextGray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                text = "Edit Maintenance Log",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        }

        Text(
            text = "Update the operational records, notes, and milestones for this specific diagnostic entry below.",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = "MAINTENANCE / SERVICE TYPE",
                        color = LightSubtext,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = serviceType,
                        onValueChange = { viewModel.onServiceTypeChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = LightBlueField,
                            unfocusedContainerColor = LightBlueField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("e.g. Engine Oil Change", color = TextGray) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "SERVICE DATE",
                        color = LightSubtext,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(LightBlueField, RoundedCornerShape(12.dp))
                            .clickable { showDatePicker = true }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rawDateText.ifBlank { "Select maintenance date..." },
                                color = if (rawDateText.isBlank()) TextGray else Color.Black,
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pick Date",
                                tint = TextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "ADDITIONAL NOTES / DESCRIPTION",
                        color = LightSubtext,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = notes,
                        onValueChange = { viewModel.onNotesChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = LightBlueField,
                            unfocusedContainerColor = LightBlueField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Write hardware changes, diagnostic notes...", color = TextGray) },
                        maxLines = 5
                    )
                }

                Button(
                    onClick = {
                        val currentTimestamp = selectedTimestamp
                        if (serviceType.isNotBlank() && currentTimestamp != null) {
                            viewModel.updateLog(
                                logId = log.id,
                                vehicleId = log.associateVehicleId,
                                type = serviceType.trim(),
                                notes = notes.trim(),
                                date = currentTimestamp,
                                isCompleted = log.isCompleted
                            )
                        }
                    },
                    enabled = serviceType.isNotBlank() && selectedTimestamp != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlueCard,
                        disabledContainerColor = DarkBlueCard.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Maintenance Log", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}