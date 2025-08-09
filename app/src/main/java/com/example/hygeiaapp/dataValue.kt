package com.example.hygeiaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme

private val normalValuesMap = mapOf(
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

private fun isValueNormal(label: String, value: String): Boolean {
    val lowercasedValue = value.lowercase()
    return when (label) {
        "Leukosit", "Nitrit", "Protein", "Darah", "Keton", "Bilirubin", "Glukosa" ->
            lowercasedValue == "neg" || lowercasedValue == "negatif"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPage(qrResult: String?) {
    // State untuk menyimpan hasil yang sudah diurai dan dianalisis
    var urinalysisResult by remember { mutableStateOf<UrinalysisResult?>(null) }
    var analysis by remember { mutableStateOf<Pair<String, String>?>(null) }

    // LaunchedEffect untuk mengurai data sekali saat diterima

    LaunchedEffect(qrResult) {
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

    if (urinalysisResult == null || analysis == null) {

    } else {
        Scaffold(
            topBar = { NavBar() },
        ){ paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Bagian Indikator Penyakit dan Saran
            item {
                Text(
                    "Hasil Analisis",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Card untuk Indikator
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Diagnosis Awal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(analysis!!.first, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Card untuk Saran
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Saran",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(analysis!!.second, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Detail Parameter",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bagian Detail Parameter
            item {
                ParameterRow(
                    "Leukosit",
                    urinalysisResult!!.leukocytes,
                    normalValuesMap["Leukosit"]!!
                )
            }
            item { ParameterRow("Nitrit", urinalysisResult!!.nitrite, normalValuesMap["Nitrit"]!!) }
            item {
                ParameterRow(
                    "Urobilinogen",
                    urinalysisResult!!.urobilinogen,
                    normalValuesMap["Urobilinogen"]!!
                )
            }
            item {
                ParameterRow(
                    "Protein",
                    urinalysisResult!!.protein,
                    normalValuesMap["Protein"]!!
                )
            }
            item { ParameterRow("pH", urinalysisResult!!.pH, normalValuesMap["pH"]!!) }
            item { ParameterRow("Darah", urinalysisResult!!.blood, normalValuesMap["Darah"]!!) }
            item {
                ParameterRow(
                    "Berat Jenis",
                    urinalysisResult!!.specificGravity,
                    normalValuesMap["Berat Jenis"]!!
                )
            }
            item { ParameterRow("Keton", urinalysisResult!!.ketone, normalValuesMap["Keton"]!!) }
            item {
                ParameterRow(
                    "Bilirubin",
                    urinalysisResult!!.bilirubin,
                    normalValuesMap["Bilirubin"]!!
                )
            }
            item {
                ParameterRow(
                    "Glukosa",
                    urinalysisResult!!.glucose,
                    normalValuesMap["Glukosa"]!!
                )
            }
        }
    }
    }
}
@Composable
private fun ParameterRow(label: String, value: String, normalValue: String) {
    val isNormal = isValueNormal(label, value)
    val indicatorColor = if (isNormal) Color(0xFF388E3C) else Color(0xFFD32F2F) // Hijau atau Merah

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label Parameter
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Memberi bobot agar label bisa memanjang
        )

        // Kolom untuk Nilai, Indikator, dan Nilai Normal
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Indikator Titik Berwarna
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Nilai Hasil Pengukuran
                Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
            // Teks Nilai Normal di bawahnya
            Text(
                text = "Normal: $normalValue",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
    Divider()
}
@Preview(name = "Kondisi dengan Indikasi", showBackground = true)
@Composable
fun ResultPageIndicationPreview() {
    // Ganti HygeiaAppTheme dengan nama tema aplikasi Anda jika berbeda
    HygeiaAppTheme {
        // Data sampel yang akan memicu logika "Potensi ISK"
        val sampleData = "Moderate, neg, 16, Trace, 6.5, Neg, 1.020, Trace, Neg., Glucose"
        ResultPage(qrResult = sampleData)
    }
}