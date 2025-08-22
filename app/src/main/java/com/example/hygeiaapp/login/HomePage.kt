package com.example.hygeiaapp.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hygeiaapp.login.AuthViewModel
import com.example.hygeiaapp.NavBar
import com.example.hygeiaapp.ui.theme.HygeiaAppTheme

// Data class untuk menampung data tips kesehatan
data class HealthTip(
    val title: String,
    val description: String
)

// Daftar tips kesehatan (bisa diambil dari ViewModel atau API nantinya)
val healthTipsList = listOf(
    HealthTip("Hidrasi adalah Kunci", "Pastikan minum 8-10 gelas air setiap hari. Hygeia dapat membantu memantau tingkat hidrasi Anda melalui warna dan konsentrasi urine."),
    HealthTip("Nutrisi Seimbang", "Konsumsi beragam buah, sayur, protein, dan karbohidrat kompleks. Apa yang Anda makan akan tercermin pada hasil analisis Hygeia."),
    HealthTip("Pentingnya Istirahat", "Tidur 7-8 jam setiap malam membantu regenerasi sel dan menjaga keseimbangan hormon. Kurang tidur dapat memengaruhi kesehatan Anda secara keseluruhan."),
    HealthTip("Aktivitas Fisik Teratur", "Lakukan olahraga ringan hingga sedang setidaknya 30 menit setiap hari. Ini membantu meningkatkan metabolisme dan kesehatan jantung."),
    HealthTip("Jangan Abaikan Sinyal Tubuh", "Hygeia adalah alat bantu. Jika Anda merasa tidak sehat atau hasil menunjukkan anomali, segera konsultasikan dengan dokter profesional.")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    Scaffold(
        topBar = { NavBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("qr")
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Filled.QrCodeScanner,
                    contentDescription = "Scan QR Code",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    ) { paddingValues ->
        // LazyColumn digunakan untuk membuat daftar yang bisa di-scroll secara efisien
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Bagian 1: Hero Section
            item {
                HeroSection()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bagian 2: Keterangan Produk
            item {
                ProductDescriptionSection()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bagian 3: Tips-tips Kesehatan
            item {
                Text(
                    text = "Tips Kesehatan dari Hygeia",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(healthTipsList) { tip ->
                HealthTipCard(tip = tip)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp)) // Extra space at the bottom
            }
        }
    }
}

@Composable
fun HeroSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Hygeia",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Kesehatan Anda, Terdeteksi Dini",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProductDescriptionSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Apa itu Hygeia?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Hygeia adalah urinoir pintar revolusioner yang dirancang untuk memantau kesehatan Anda secara real-time. Dengan sensor canggih, Hygeia menganalisis parameter kunci dalam urine untuk memberikan deteksi dini, wawasan kesehatan, dan saran yang dipersonalisasi langsung ke aplikasi smartphone Anda. Ini adalah cara termudah untuk menjadikan pengecekan kesehatan sebagai bagian dari rutinitas harian Anda.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun HealthTipCard(tip: HealthTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tip.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tip.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Preview untuk melihat tampilan di Android Studio
@Preview(showBackground = true)
@Composable
fun HygeiaHomeScreenPreview() {
    HygeiaAppTheme {
        HomePage(navController = rememberNavController())
    }
}