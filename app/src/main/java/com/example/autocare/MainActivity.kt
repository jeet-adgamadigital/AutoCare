package com.example.autocare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocare.ui.features.SplashScreen
import com.example.autocare.ui.features.SplashScreenViewModel
import com.example.autocare.ui.navigation.NavRoutes
import com.example.autocare.ui.theme.AutoCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val container = (applicationContext as AutoCareApp).container
        val sessionManager = container.sessionManager

        //Factory
        val splashFactory = SplashScreenViewModel.Factory(sessionManager)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoCareTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Splash
                ){
                    composable<NavRoutes.Splash>{
                        SplashScreen(
                            onNavigateToHome = {
                                navController.navigate(NavRoutes.Home)
                            },
                            onNavigateToSignUp = {
                                navController.navigate(NavRoutes.Auth)
                            },
                            factory = splashFactory
                        )
                    }

                    composable<NavRoutes.Auth>{
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ){
                            Text("Auth Screen")
                        }
                    }

                    composable<NavRoutes.Home> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Home Screen")
                        }
                    }
                }
            }
        }
    }
}
