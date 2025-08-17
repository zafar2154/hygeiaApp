package com.example.hygeiaapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hygeiaapp.ui.screen.HomePage
import java.net.URLDecoder


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
        composable("result/{qrResult}",
                arguments = listOf(navArgument("qrResult"
                ) {
                    type = NavType.StringType
                }) // 2. Specify the argument type
        ) {
            backStackEntry ->
            val encodedData = backStackEntry.arguments?.getString("qrResult") ?: ""
            val decodedData = URLDecoder.decode(encodedData, "UTF-8") // Decode the data
            ResultPage(qrResult = decodedData)
            }
        }
    }
