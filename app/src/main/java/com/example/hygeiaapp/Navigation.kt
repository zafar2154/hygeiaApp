package com.example.hygeiaapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hygeiaapp.ui.screen.HomePage


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Qr : Screen("qr")
    object Result : Screen("result")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginPage(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomePage(navController)
        }
        composable(Screen.Qr.route) {
            QRCodeScannerScreen(navController)
        }
        composable("result/{qrResult}") {
            backStackEntry -> val result = backStackEntry.arguments?.getString("qrResult") ?: ""
            ResultPage(result)
        }
    }
}
