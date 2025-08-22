package com.example.hygeiaapp.scan

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import java.net.URLEncoder

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRCodeScannerScreen(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val scannerBoxSize = 250.dp

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview { result ->
                val encodedResult = URLEncoder.encode(result, "UTF-8")
                navController.navigate("result/${encodedResult}") {
                    launchSingleTop = true
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Izinkan akses kamera untuk scan QRcode")
            }
        }

        ScannerOverlay(boxSize = scannerBoxSize)

        var hintVisible by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(3000)
            hintVisible = false
        }

        AnimatedVisibility(
            visible = hintVisible,
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            PinchToZoomHintAnimation()
        }
    }
}