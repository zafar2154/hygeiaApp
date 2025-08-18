package com.example.hygeiaapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // State untuk UI, memberi tahu apakah ada proses yang sedang berjalan (loading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State untuk menampung pesan error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // State untuk status login pengguna
    private val _userLoggedIn = MutableStateFlow(auth.currentUser != null)
    val userLoggedIn: StateFlow<Boolean> = _userLoggedIn

    /**
     * Fungsi untuk mendaftarkan pengguna baru.
     * @param email Email pengguna.
     * @param password Password pengguna.
     * @param onSuccess Callback yang dipanggil jika pendaftaran berhasil.
     */
    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _userLoggedIn.value = true
                    Log.d("AuthViewModel", "Pendaftaran berhasil.")
                    onSuccess()
                } else {
                    _errorMessage.value = task.exception?.message ?: "Pendaftaran gagal."
                    Log.w("AuthViewModel", "Pendaftaran gagal.", task.exception)
                }
            }
    }

    /**
     * Fungsi untuk login pengguna.
     * @param email Email pengguna.
     * @param password Password pengguna.
     * @param onSuccess Callback yang dipanggil jika login berhasil.
     */
    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _userLoggedIn.value = true
                    Log.d("AuthViewModel", "Login berhasil.")
                    onSuccess()
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login gagal."
                    Log.w("AuthViewModel", "Login gagal.", task.exception)
                }
            }
    }

    /**
     * Fungsi untuk logout pengguna.
     */
    fun signOut() {
        auth.signOut()
        _userLoggedIn.value = false
    }

    /**
     * Fungsi untuk membersihkan pesan error.
     */
    fun clearError() {
        _errorMessage.value = null
    }
}