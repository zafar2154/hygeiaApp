package com.example.hygeiaapp.result

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hygeiaapp.NavBar
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPage(qrResult: String?, resultViewModel: ResultViewModel = viewModel()) {

    LaunchedEffect(qrResult) {
        resultViewModel.processQrResult(qrResult)
    }

    val urinalysisResult = resultViewModel.urinalysisResult
    val analysis = resultViewModel.analysis

    if (urinalysisResult == null || analysis == null) {
        // Tampilkan loading atau pesan lain jika data belum siap
    } else {
        Scaffold(
            topBar = { NavBar() },
        ) { paddingValues ->
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
                            Text(analysis.first, style = MaterialTheme.typography.bodyLarge)
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
                            Text(analysis.second, style = MaterialTheme.typography.bodyLarge)
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
                        urinalysisResult.leukocytes,
                        resultViewModel.normalValuesMap["Leukosit"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Nitrit",
                        urinalysisResult.nitrite,
                        resultViewModel.normalValuesMap["Nitrit"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Urobilinogen",
                        urinalysisResult.urobilinogen,
                        resultViewModel.normalValuesMap["Urobilinogen"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Protein",
                        urinalysisResult.protein,
                        resultViewModel.normalValuesMap["Protein"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "pH",
                        urinalysisResult.pH,
                        resultViewModel.normalValuesMap["pH"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Darah",
                        urinalysisResult.blood,
                        resultViewModel.normalValuesMap["Darah"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Berat Jenis",
                        urinalysisResult.specificGravity,
                        resultViewModel.normalValuesMap["Berat Jenis"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Keton",
                        urinalysisResult.ketone,
                        resultViewModel.normalValuesMap["Keton"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Bilirubin",
                        urinalysisResult.bilirubin,
                        resultViewModel.normalValuesMap["Bilirubin"]!!,
                        resultViewModel::isValueNormal
                    )
                }
                item {
                    ParameterRow(
                        "Glukosa",
                        urinalysisResult.glucose,
                        resultViewModel.normalValuesMap["Glukosa"]!!,
                        resultViewModel::isValueNormal
                    )
                }
            }
        }
    }
}

@Composable
private fun ParameterRow(
    label: String,
    value: String,
    normalValue: String,
    isNormalCheck: (String, String) -> Boolean
) {
    val isNormal = isNormalCheck(label, value)
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
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
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
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
}

@Preview(name = "Kondisi dengan Indikasi", showBackground = true)
@Composable
fun ResultPageIndicationPreview() {
    HygeiaAppTheme {
        val sampleData = "Moderate, neg, 16, Trace, 6.5, Neg, 1.020, Trace, Neg., Glucose"
        ResultPage(qrResult = sampleData)
    }
}