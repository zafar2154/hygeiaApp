package com.example.hygeiaapp

import android.Manifest
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.net.URLEncoder
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.delay

@kotlin.OptIn(ExperimentalPermissionsApi::class)
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

        // --- HANYA OVERLAY SCANNER ---
        ScannerOverlay(boxSize = scannerBoxSize)

        // --- PETUNJUK ANIMASI ZOOM ---
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

@Composable
private fun ScannerOverlay(boxSize: androidx.compose.ui.unit.Dp) {
    val boxSizePx = with(LocalDensity.current) { boxSize.toPx() }
    val cornerLength = with(LocalDensity.current) { 30.dp.toPx() }
    val strokeWidth = with(LocalDensity.current) { 4.dp.toPx() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val rectTopLeft = Offset((canvasWidth - boxSizePx) / 2, (canvasHeight - boxSizePx) / 2)
        val rectSize = Size(boxSizePx, boxSizePx)

        // Gambar overlay gelap di seluruh layar
        drawRect(color = Color.Black.copy(alpha = 0.6f))

        // "Potong" bagian tengah untuk area pemindaian
        drawRoundRect(
            topLeft = rectTopLeft,
            size = rectSize,
            cornerRadius = CornerRadius(16.dp.toPx()),
            color = Color.Transparent,
            blendMode = BlendMode.Clear
        )

        // Gambar 4 sudut bingkai
        val path = androidx.compose.ui.graphics.Path().apply {
            // Top-left
            moveTo(rectTopLeft.x, rectTopLeft.y + cornerLength)
            lineTo(rectTopLeft.x, rectTopLeft.y)
            lineTo(rectTopLeft.x + cornerLength, rectTopLeft.y)
            // Top-right
            moveTo(rectTopLeft.x + boxSizePx - cornerLength, rectTopLeft.y)
            lineTo(rectTopLeft.x + boxSizePx, rectTopLeft.y)
            lineTo(rectTopLeft.x + boxSizePx, rectTopLeft.y + cornerLength)
            // Bottom-left
            moveTo(rectTopLeft.x, rectTopLeft.y + boxSizePx - cornerLength)
            lineTo(rectTopLeft.x, rectTopLeft.y + boxSizePx)
            lineTo(rectTopLeft.x + cornerLength, rectTopLeft.y + boxSizePx)
            // Bottom-right
            moveTo(rectTopLeft.x + boxSizePx - cornerLength, rectTopLeft.y + boxSizePx)
            lineTo(rectTopLeft.x + boxSizePx, rectTopLeft.y + boxSizePx)
            lineTo(rectTopLeft.x + boxSizePx, rectTopLeft.y + boxSizePx - cornerLength)
        }

        drawPath(
            path = path,
            color = Color.Red,
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun PinchToZoomHintAnimation() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "pinch_transition")
                val distance by infiniteTransition.animateFloat(
                    initialValue = 15f,
                    targetValue = 40f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = "pinch_distance"
                )
                Box(
                    Modifier
                        .offset(x = (-distance).dp, y = (-distance).dp)
                        .size(30.dp)
                        .border(2.dp, Color.White, CircleShape)
                )
                Box(
                    Modifier
                        .offset(x = distance.dp, y = distance.dp)
                        .size(30.dp)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pinch to Zoom",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}


@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val hasScanned = remember { AtomicBoolean(false) }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize()) { view ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(view.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scannerOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(scannerOptions)

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanner.process(inputImage)
                        .addOnSuccessListener { barcodes ->
                            barcodes.firstOrNull()?.rawValue?.let {
                                if (hasScanned.compareAndSet(false, true)) {
                                    onQRCodeScanned(it)
                                    Log.d("QRCodeScanner", "QR Code: $it")
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("QRCodeScanner", "Error: ${it.message}")
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
                val cameraZoom = CameraZoom(camera, ContextCompat.getMainExecutor(context))
                cameraZoom.setupPinchToZoom(view)

            } catch(exc: Exception) {
                Log.e("QRCodeScanner", "Gagal melakukan binding", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }
}
