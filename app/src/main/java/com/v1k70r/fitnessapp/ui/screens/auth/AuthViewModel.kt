package com.v1k70r.fitnessapp.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FacebookAuthProvider

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var uiState by mutableStateOf(
        AuthUiState(isAuthenticated = auth.currentUser != null)
    )
        private set

    fun iniciarSesionConEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa todos los campos.")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState(isLoading = true)

            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                uiState = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                uiState = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al iniciar sesión."
                )
            }
        }
    }

    fun crearCuentaConEmail(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa todos los campos.")
            return
        }

        if (password != confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState(isLoading = true)

            try {
                auth.createUserWithEmailAndPassword(email.trim(), password).await()
                uiState = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                uiState = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al crear la cuenta."
                )
            }
        }
    }

    fun iniciarSesionConGoogle(idToken: String) {
        viewModelScope.launch {
            uiState = AuthUiState(isLoading = true)

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                uiState = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                uiState = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al iniciar con Google."
                )
            }
        }
    }

    fun limpiarError() {
        uiState = uiState.copy(errorMessage = null)
    }

    fun mostrarError(message: String) {
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    fun cerrarSesion() {
        auth.signOut()
        uiState = AuthUiState(isAuthenticated = false)
    }

    fun iniciarSesionConFacebook(token: String) {

        viewModelScope.launch {

            uiState = AuthUiState(isLoading = true)

            try {

                val credential = FacebookAuthProvider.getCredential(token)

                auth.signInWithCredential(credential).await()

                uiState = AuthUiState(
                    isAuthenticated = true
                )

            } catch (e: Exception) {

                uiState = AuthUiState(
                    errorMessage = e.localizedMessage
                        ?: "Error al iniciar sesión con Facebook."
                )
            }
        }
    }
}