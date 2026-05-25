package com.v1k70r.fitnessapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FacebookAuthProvider

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(
        AuthUiState(isAuthenticated = auth.currentUser != null)
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun iniciarSesionConEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Completa todos los campos.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al iniciar sesión."
                )
            }
        }
    }

    fun crearCuentaConEmail(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Completa todos los campos.")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                auth.createUserWithEmailAndPassword(email.trim(), password).await()
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al crear la cuenta."
                )
            }
        }
    }

    fun iniciarSesionConGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.localizedMessage ?: "Error al iniciar con Google."
                )
            }
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun mostrarError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    fun cerrarSesion() {
        auth.signOut()
        _uiState.value = AuthUiState(isAuthenticated = false)
    }

    fun iniciarSesionConFacebook(token: String) {

        viewModelScope.launch {

            _uiState.value = AuthUiState(isLoading = true)

            try {

                val credential = FacebookAuthProvider.getCredential(token)

                auth.signInWithCredential(credential).await()

                _uiState.value = AuthUiState(
                    isAuthenticated = true
                )

            } catch (e: Exception) {

                _uiState.value = AuthUiState(
                    errorMessage = e.localizedMessage
                        ?: "Error al iniciar sesión con Facebook."
                )
            }
        }
    }
}