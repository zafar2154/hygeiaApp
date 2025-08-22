package com.example.hygeiaapp.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hygeiaapp.login.AuthViewModel
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Mengamati state dari ViewModel
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tampilkan pesan error jika ada
        errorMessage?.let {
            Text(text = it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                authViewModel.clearError() // Bersihkan error lama sebelum mencoba lagi
                authViewModel.signIn(email, password) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            enabled = !isLoading // Tombol dinonaktifkan saat loading
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Login")
        }

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Belum punya akun? Daftar di sini")
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Daftar Akun", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                authViewModel.clearError()
                authViewModel.signUp(email, password) {
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            },
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Daftar")
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Sudah punya akun? Login")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    HygeiaAppTheme {
        SignUpScreen(navController = rememberNavController())
    }
}