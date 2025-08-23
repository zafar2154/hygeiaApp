package com.example.hygeiaapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hygeiaapp.login.AuthViewModel
import com.example.hygeiaapp.login.HomePage
import com.example.hygeiaapp.login.LoginScreen
import com.example.hygeiaapp.login.SignUpScreen
import com.example.hygeiaapp.result.ResultPage
import com.example.hygeiaapp.scan.QRCodeScannerScreen
import java.net.URLDecoder


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Qr : Screen("qr")
    object SignUp : Screen("signup")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel = AuthViewModel()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController, authViewModel)
        }
        composable(Screen.Home.route) {
            HomePage(navController, authViewModel)
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
