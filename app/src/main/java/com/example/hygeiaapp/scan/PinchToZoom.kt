package com.example.hygeiaapp.scan

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinchToZoomHintAnimation() {
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