package com.v1k70r.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.v1k70r.fitnessapp.ui.navigation.MainScaffold
import com.v1k70r.fitnessapp.ui.screens.auth.AuthScreen
import com.v1k70r.fitnessapp.ui.screens.auth.AuthViewModel
import com.v1k70r.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessAppTheme {
                var isAuthenticated by remember {
                    mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
                }

                val authViewModel: AuthViewModel = viewModel()

                if (isAuthenticated) {
                    MainScaffold(
                        onLogout = {
                            authViewModel.cerrarSesion()
                            isAuthenticated = false
                        }
                    )
                } else {
                    AuthScreen(
                        authViewModel = authViewModel,
                        onAuthenticated = { isAuthenticated = true }
                    )
                }
            }
        }
    }
}