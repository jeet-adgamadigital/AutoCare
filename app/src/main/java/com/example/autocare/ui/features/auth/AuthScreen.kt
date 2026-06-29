package com.example.autocare.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autocare.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    factory: AuthScreenViewModel.Factory
) {
    val viewModel: AuthScreenViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiStates.collectAsStateWithLifecycle()
    val apiState by viewModel.apiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                AuthScreenViewModel.AuthNav.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13223C)) // Dark Blue Top Background
    ) {
        // --- Header Section ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 40.dp, end = 24.dp, bottom = 28.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("AutoCare", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text("MAINTENANCE TRACKER", color = Color(0xFF6B7C96), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Stay ahead of every service — log history, track schedules, get notified.",
                color = Color(0xFFBACAD6),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FeatureTag(label = "Oil Change")
                FeatureTag(label = "Tyre Check")
                FeatureTag(label = "Insurance")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = Color(0xFFF5F6F8),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column {
                TabRowSelector(uiState = uiState, onTabSelected = { selectedState ->
                    if (uiState != selectedState) {
                        viewModel.clearInput()
                        if (selectedState == AuthScreenViewModel.AuthUiStates.SignIn) {
                            viewModel.onMailChange("")
                        }
                        viewModel.changeState(selectedState)
                    }
                })

                Spacer(modifier = Modifier.height(24.dp))
                if (apiState is AuthScreenViewModel.AuthApiState.Error) {
                    Text(
                        text = (apiState as AuthScreenViewModel.AuthApiState.Error).message,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                when (uiState) {
                    AuthScreenViewModel.AuthUiStates.SignIn -> {
                        SignInScreen(viewModel = viewModel, apiState = apiState)
                    }
                    AuthScreenViewModel.AuthUiStates.SignUp -> {
                        SignUpScreen(viewModel = viewModel, apiState = apiState)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureTag(label: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color(0.2f, 0.3f, 0.4f, 0.4f), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = Color(0xFF8A99AD), fontSize = 12.sp)
    }
}

@Composable
fun TabRowSelector(
    uiState: AuthScreenViewModel.AuthUiStates,
    onTabSelected: (AuthScreenViewModel.AuthUiStates) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(Color(0xFFE9ECF0), RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        val tabs = listOf(
            AuthScreenViewModel.AuthUiStates.SignIn to "Sign In",
            AuthScreenViewModel.AuthUiStates.SignUp to "Register"
        )
        tabs.forEach { (tabState, title) ->
            val isSelected = uiState == tabState
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (isSelected) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onTabSelected(tabState) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (isSelected) Color(0xFF192A46) else Color(0xFF6B7C96),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}