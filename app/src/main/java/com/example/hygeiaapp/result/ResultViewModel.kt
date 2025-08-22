package com.example.hygeiaapp.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    var urinalysisResult by mutableStateOf<UrinalysisResult?>(null)
        private set

    var analysis by mutableStateOf<Pair<String, String>?>(null)
        private set

    val normalValuesMap = mapOf(
        "Leukosit" to "Negatif",
        "Nitrit" to "Negatif",
        "Urobilinogen" to "0.2 - 1.0 mg/dL",
        "Protein" to "Negatif",
        "pH" to "5.0 - 8.0",
        "Darah" to "Negatif",
        "Berat Jenis" to "1.005 - 1.030",
        "Keton" to "Negatif",
        "Bilirubin" to "Negatif",
        "Glukosa" to "Negatif"
    )

    fun processQrResult(qrResult: String?) {
        if (!qrResult.isNullOrEmpty()) {
            val parts = qrResult.split(",").map { it.trim() }
            if (parts.size >= 10) {
                val resultData = UrinalysisResult(
                    leukocytes = parts[0],
                    nitrite = parts[1],
                    urobilinogen = parts[2],
                    protein = parts[3],
                    pH = parts[4],
                    blood = parts[5],
                    specificGravity = parts[6],
                    ketone = parts[7],
                    bilirubin = parts[8],
                    glucose = parts[9]
                )
                urinalysisResult = resultData
                analysis = analyzeResult(resultData)
            }
        }
    }

    fun isValueNormal(label: String, value: String): Boolean {
        val lowercasedValue = value.lowercase()
        return when (label) {
            "Leukosit", "Nitrit", "Protein", "Darah", "Keton", "Bilirubin", "Glukosa" ->
                lowercasedValue == "neg" || lowercasedValue == "negatif" || lowercasedValue == "neg."
            "pH" -> {
                val numValue = lowercasedValue.toDoubleOrNull()
                numValue != null && numValue in 5.0..8.0
            }
            "Berat Jenis" -> {
                val numValue = lowercasedValue.toDoubleOrNull()
                numValue != null && numValue in 1.005..1.030
            }
            // Untuk Urobilinogen, kita anggap semua nilai yang terdeteksi perlu perhatian
            else -> false
        }
    }

    private fun analyzeResult(result: UrinalysisResult): Pair<String, String> {
        // Implementasi analisis Anda di sini
        // Contoh:
        if (result.leukocytes.lowercase() != "negatif" && result.nitrite.lowercase() != "negatif") {
            return Pair("Potensi Infeksi Saluran Kemih (ISK)", "Segera konsultasikan dengan dokter untuk diagnosis lebih lanjut.")
        }
        return Pair("Tidak ada indikasi penyakit serius", "Tetap jaga kesehatan dan pola makan.")
    }
}