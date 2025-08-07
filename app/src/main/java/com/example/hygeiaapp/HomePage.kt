package com.example.hygeiaapp

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator

@Composable
fun HomePage(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult = IntentIntegrator.parseActivityResult(
                result.resultCode, result.data
            )
            val qrResult = intentResult?.contents
            // TODO: Lakukan sesuatu dengan hasil QR
            Log.d("QR_RESULT", "Hasil: $qrResult")
        }
    }
    Scaffold(
        topBar = { NavBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hygiea",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Health Check Urinoir",
                fontSize = 16.sp,
                color = Color.Gray
            )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tentang Proyek
                Text(
                    text = "Tentang Alat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sistem monitoring kesehatan dan deteksi dini penyakit berbasis urinalysis dengan menggunakan artificial Intelligence dan Internet of Things",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                // Tips Sehat
                Text(
                    text = "Tips Hidup Sehat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val tips = listOf(
                    "Minum air putih minimal 2 liter per hari.",
                    "Perhatikan warna urin — semakin jernih, semakin baik.",
                    "Kurangi konsumsi makanan tinggi garam dan gula.",
                    "Rutin berolahraga minimal 3 kali seminggu.",
                    "Jangan menahan buang air kecil terlalu lama.",
                    "Lakukan pemeriksaan urin secara berkala."
                )

                tips.forEach { tip ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text("•", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tip, fontSize = 14.sp)
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val integrator = IntentIntegrator(activity).apply {
                        setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        setPrompt("Scan QR Code")
                        setBeepEnabled(true)
                        setCameraId(0)
                        setOrientationLocked(false)
                        setBarcodeImageEnabled(true)
                    }
                    launcher.launch(integrator.createScanIntent())
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Scan QR", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan QR Code", color = Color.White)
            }
            }
            }
        }


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HygeiaAppTheme {
        HomePage(navController = rememberNavController())
    }
}