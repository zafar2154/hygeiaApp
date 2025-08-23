package com.example.hygeiaapp.result

import android.os.Build
import android.util.Log // <-- PASTIKAN ANDA MENGIMPOR LOG
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hygeiaapp.scan.decryptAES

class ResultViewModel : ViewModel() {

    var urinalysisResult by mutableStateOf<UrinalysisResult?>(null)
        private set

    var analysis by mutableStateOf<Pair<String, String>?>(null)
        private set

    // ... (properti lain tetap sama)
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun processQrResult(qrResult: String?) {
        // --- MULAI DEBUG ---
        Log.d("PROCESS_DEBUG", "1. Memulai processQrResult...")

        if (qrResult.isNullOrEmpty()) {
            Log.w("PROCESS_DEBUG", "2. Gagal: qrResult null atau kosong.")
            return
        }
        val qrResultClean = qrResult.replace(" ","+")

        Log.d("PROCESS_DEBUG", "2. Menerima data QR (terenkripsi): $qrResultClean")

        try {
            val key = "opadfahadfladfaj".toByteArray(Charsets.UTF_8)
            Log.d("PROCESS_DEBUG", "3. Mencoba dekripsi dengan kunci...")

            val decryptedText = decryptAES(qrResultClean, key)
            Log.d("PROCESS_DEBUG", "4. SUKSES! Hasil dekripsi: $decryptedText")

            decryptedText?.let { text ->
                // Di dalam blok ini, 'text' dijamin tidak null.
                Log.d("PROCESS_DEBUG", "SUKSES! Hasil dekripsi: $text")

                val parts = text.split(",").map { it.trim() }
                Log.d("PROCESS_DEBUG", "Data dipecah menjadi ${parts.size} bagian: $parts")

                if (parts.size >= 10) {
                    Log.d("PROCESS_DEBUG", "Jumlah data valid, membuat objek UrinalysisResult...")
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
                    Log.d("PROCESS_DEBUG", "State berhasil diperbarui!")
                } else {
                    Log.e("PROCESS_DEBUG", "GAGAL: Jumlah data setelah dipecah kurang dari 10.")
                }
            } ?: run {
                // Blok ini akan dijalankan JIKA decryptedText adalah null
                Log.e("PROCESS_DEBUG", "GAGAL: Hasil dekripsi adalah null.")
            }

        } catch (e: Exception) {
            // Ini akan menangkap error jika dekripsi GAGAL TOTAL (misal: BadPaddingException)
            Log.e("PROCESS_DEBUG", "DEKRIPSI GAGAL TOTAL!", e)
        }
    }

    // ... (sisa fungsi Anda)
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