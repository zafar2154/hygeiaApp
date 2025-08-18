package com.example.hygeiaapp

// Hapus atau komentari impor Android Base64 jika hanya untuk main()
// import android.util.Base64

// Gunakan impor Java Base64
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64 // Untuk Java 8+

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
// import androidx.camera.core.Logger // Jika tidak digunakan di main, bisa dikomentari


@RequiresApi(Build.VERSION_CODES.O)
fun encryptAES(plainText: String, key: ByteArray): String {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val secretKey: SecretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val iv = cipher.iv
    val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8)) // Pastikan charset konsisten

    // Gunakan Java Base64 untuk encoding
    val encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes)
    val ivBase64 = Base64.getEncoder().encodeToString(iv)

    return "$encryptedBase64:$ivBase64"
}

@RequiresApi(Build.VERSION_CODES.O)
fun decryptAES(encryptedText: String, key: ByteArray): String? {
    try {
        val parts = encryptedText.split(":")
        if (parts.size != 2) {
            System.err.println("DECRYPT_ERROR: Invalid encrypted format. Expected 2 parts, got ${parts.size}")
            return null
        }

        val encryptedDataString = parts[0].trim()
        val ivString = parts[1].trim()

        // Gunakan Java Base64 untuk decoding
        val encryptedData = Base64.getDecoder().decode(encryptedDataString)
        val iv = Base64.getDecoder().decode(ivString)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv) // 128 adalah panjang tag dalam bit
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
        val decryptedBytes = cipher.doFinal(encryptedData)
        return String(decryptedBytes, Charsets.UTF_8) // Pastikan charset konsisten

    } catch (e: Exception) {
        System.err.println("DECRYPT_ERROR: Decryption failed for text: '$encryptedText'")
        e.printStackTrace()
        return null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val key = "opadfahadfladfaj".toByteArray(Charsets.UTF_8) // Pastikan charset konsisten (16 byte -> AES-128)
    val plainText = "Hello, AES in GCM!"
    // val enkripsi = "yqnKO5wGvmr+hcrQUs60NCMOsmDTTQnujfpDoRDPje4Dxi3s7LoVHy/0NDaUL2a652gGxRI=:KREtEjew6bCA4Ug2yU8qcA=="

    println("Original: $plainText")

    val encryptedText = encryptAES(plainText, key)
    println("Encrypted: $encryptedText")

    val decode = "yqnKO5wGvmr+hcrQUs60NCMOsmDTTQnujfpDoRDPje4Dxi3s7LoVHy/0NDaUL2a652gGxRI=:KREtEjew6bCA4Ug2yU8qcA=="
    val decryptedText = decryptAES(decode, key)
    println("Decrypted: $decryptedText")

    if (plainText == decryptedText) {
        println("SUCCESS: Plaintext matches decrypted text.")
    } else {
        println("FAILURE: Plaintext does NOT match decrypted text.")
    }

    // Contoh dekripsi string yang sudah ada (pastikan ini dienkripsi dengan key yang sama dan IV yang sesuai)
    // val knownEncrypted = "YOUR_PREVIOUSLY_ENCRYPTED_STRING_HERE"
    // val decryptedKnown = decryptAES(knownEncrypted, key)
    // println("Decrypted known: $decryptedKnown")
}
