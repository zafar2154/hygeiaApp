package com.example.hygeiaapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

data class UrinalysisResult(
    val leukocytes: String = "N/A",
    val nitrite: String = "N/A",
    val urobilinogen: String = "N/A",
    val protein: String = "N/A",
    val pH: String = "N/A",
    val blood: String = "N/A",
    val specificGravity: String = "N/A",
    val ketone: String = "N/A",
    val bilirubin: String = "N/A",
    val glucose: String = "N/A"
)

    fun analyzeResult(result: UrinalysisResult): Pair<String, String> {
        // Ini adalah contoh logika yang sangat sederhana.
        // Aplikasi nyata akan membutuhkan aturan medis yang kompleks.
        if (result.leukocytes.contains("Moderate", ignoreCase = true) ||
            result.leukocytes.contains("Trace", ignoreCase = true) ||
            result.nitrite.contains("pos", ignoreCase = true)
        ) {
            return Pair(
                "Potensi Infeksi Saluran Kemih (ISK)",
                "Hasil menunjukkan adanya kemungkinan infeksi. Disarankan untuk minum lebih banyak air putih dan segera konsultasikan dengan dokter untuk diagnosis dan penanganan lebih lanjut."
            )
        }
        if (result.glucose.contains("pos", ignoreCase = true) ||
            !result.glucose.contains("neg", ignoreCase = true)
        ) {
            return Pair(
                "Potensi Peningkatan Gula Darah",
                "Terdeteksi adanya glukosa dalam urin. Ini bisa menjadi indikasi diabetes. Segera konsultasikan dengan dokter untuk pemeriksaan gula darah dan evaluasi lebih lanjut."
            )
        }
        return Pair(
            "Tampak Normal",
            "Parameter yang diuji tidak menunjukkan kelainan signifikan. Tetap jaga pola hidup sehat. Jika Anda memiliki keluhan, tetap konsultasikan dengan dokter."
        )
    }