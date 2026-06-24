package com.example.autocare.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autocare.R // Ensure you import your R file
import com.example.autocare.ui.theme.DarkBlueBackground
import com.example.autocare.ui.theme.LightSubtext
import com.example.autocare.ui.theme.OrangeAccent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    factory: ViewModelProvider.Factory,
) {

    val viewModel: SplashScreenViewModel = viewModel(factory = factory)
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SplashScreenViewModel.SplashNav.NavigateToHome -> onNavigateToHome()
                is SplashScreenViewModel.SplashNav.NavigateToSignUp -> onNavigateToSignUp()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlueBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "AutoCare Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AutoCare",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "VEHICLE MAINTENANCE TRACKER",
                color = LightSubtext,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(48.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color = OrangeAccent, shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "CHECKING SESSION...",
                color = LightSubtext,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }
    }
}