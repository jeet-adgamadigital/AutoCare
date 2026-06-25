package com.example.autocare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocare.ui.features.auth.AuthScreen
import com.example.autocare.ui.features.auth.AuthScreenViewModel
import com.example.autocare.ui.features.home.HomeScreen
import com.example.autocare.ui.features.home.HomeViewModel
import com.example.autocare.ui.features.splash.SplashScreen
import com.example.autocare.ui.features.splash.SplashScreenViewModel
import com.example.autocare.ui.navigation.NavRoutes
import com.example.autocare.ui.theme.AutoCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val container = (applicationContext as AutoCareApp).container
        val sessionManager = container.sessionManager

        //Repositories
        val authRepository = container.authRepository
        val vehicleRepository = container.vehicleRepository
        val logsRepository = container.logsRepository
        //Factory
        val splashFactory = SplashScreenViewModel.Factory(sessionManager)
        val authFactory = AuthScreenViewModel.Factory(authRepository, sessionManager)
        val homeFactory = HomeViewModel.Factory(this.application,vehicleRepository, logsRepository)

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
                        AuthScreen(
                            onNavigateToHome = {
                                navController.navigate(NavRoutes.Home)
                            },
                            factory = authFactory
                        )
                    }

                    composable<NavRoutes.Home> {
                        HomeScreen(
                            onNavigateToSettings = {

                            },
                            factory = homeFactory
                        )
                    }
                }
            }
        }
    }
}
