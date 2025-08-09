package com.example.hygeiaapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme


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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Bagian Indikator Penyakit dan Saran
            item {
                Text("Hasil Analisis", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // Card untuk Indikator
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Indikator Penyakit", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(analysis!!.first, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Card untuk Saran
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Saran", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(analysis!!.second, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Detail Parameter", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bagian Detail Parameter
            item { ParameterRow("Leukosit", urinalysisResult!!.leukocytes) }
            item { ParameterRow("Nitrit", urinalysisResult!!.nitrite) }
            item { ParameterRow("Urobilinogen", urinalysisResult!!.urobilinogen) }
            item { ParameterRow("Protein", urinalysisResult!!.protein) }
            item { ParameterRow("pH", urinalysisResult!!.pH) }
            item { ParameterRow("Darah", urinalysisResult!!.blood) }
            item { ParameterRow("Berat Jenis", urinalysisResult!!.specificGravity) }
            item { ParameterRow("Keton", urinalysisResult!!.ketone) }
            item { ParameterRow("Bilirubin", urinalysisResult!!.bilirubin) }
            item { ParameterRow("Glukosa", urinalysisResult!!.glucose) }
        }
    }
}
@Composable
private fun ParameterRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
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