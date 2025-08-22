package com.example.hygeiaapp.scan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay(boxSize: Dp) {
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
        val path = Path().apply {
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
