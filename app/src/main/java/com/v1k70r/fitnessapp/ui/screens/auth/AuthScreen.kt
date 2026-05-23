package com.v1k70r.fitnessapp.ui.screens.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.v1k70r.fitnessapp.R
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthPrimaryButton
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthTextField
import com.v1k70r.fitnessapp.ui.screens.auth.components.SocialLoginButton
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthSecondaryButton

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthenticated: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity

    val uiState = authViewModel.uiState

    var authMode by remember { mutableStateOf(AuthMode.Login) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val googleSignInClient = remember {
        buildGoogleSignInClient(activity)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {

                val account = task.getResult(ApiException::class.java)

                val idToken = account.idToken

                if (idToken != null) {
                    authViewModel.iniciarSesionConGoogle(idToken)
                } else {
                    authViewModel.mostrarError("No se pudo obtener el token de Google.")
                }

            } catch (e: Exception) {
                authViewModel.mostrarError(
                    e.localizedMessage ?: "Error al iniciar sesión con Google."
                )
            }
        }
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthenticated()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "F",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = if (authMode == AuthMode.Login) {
                    "Iniciar sesión"
                } else {
                    "Crear cuenta"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (authMode == AuthMode.Login) {
                    "Accede para continuar con tu progreso."
                } else {
                    "Crea tu cuenta para guardar tus datos."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(36.dp))

            AuthTextField(
                value = email,
                placeholder = "Correo electrónico",
                onValueChange = {
                    email = it
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            AuthTextField(
                value = password,
                placeholder = "Contraseña",
                onValueChange = {
                    password = it
                },
                isPassword = true
            )

            if (authMode == AuthMode.Register) {

                Spacer(modifier = Modifier.height(14.dp))

                AuthTextField(
                    value = confirmPassword,
                    placeholder = "Confirmar contraseña",
                    onValueChange = {
                        confirmPassword = it
                    },
                    isPassword = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AuthPrimaryButton(
                text = if (authMode == AuthMode.Login) {
                    "Iniciar sesión"
                } else {
                    "Crear cuenta"
                },
                isLoading = uiState.isLoading,
                onClick = {

                    if (authMode == AuthMode.Login) {

                        authViewModel.iniciarSesionConEmail(
                            email = email,
                            password = password
                        )

                    } else {

                        authViewModel.crearCuentaConEmail(
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (authMode == AuthMode.Login) {
                    "¿No tienes cuenta? Crear cuenta"
                } else {
                    "¿Ya tienes cuenta? Iniciar sesión"
                },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {

                    authViewModel.limpiarError()

                    authMode =
                        if (authMode == AuthMode.Login) {
                            AuthMode.Register
                        } else {
                            AuthMode.Login
                        }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "O continúa con",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SocialLoginButton(
                    text = "G",
                    onClick = {

                        googleSignInClient.signOut()

                        launcher.launch(
                            googleSignInClient.signInIntent
                        )
                    }
                )

                FacebookLoginButton(
                    onTokenReceived = { token ->
                        authViewModel.iniciarSesionConFacebook(token)
                    },
                    onError = { error ->
                        authViewModel.mostrarError(error)
                    }
                )
            }

            uiState.errorMessage?.let { message ->

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "FitnessApp",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun buildGoogleSignInClient(
    activity: Activity
): GoogleSignInClient {

    val options = GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
        .requestIdToken(
            activity.getString(R.string.default_web_client_id)
        )
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(activity, options)
}

@Composable
private fun FacebookLoginButton(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity

    val callbackManager = remember {
        CallbackManager.Factory.create()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        callbackManager.onActivityResult(
            result.resultCode,
            result.resultCode,
            result.data
        )
    }

    DisposableEffect(Unit) {

        val callback = object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult) {
                onTokenReceived(result.accessToken.token)
            }

            override fun onCancel() {
                onError("Inicio de sesión cancelado.")
            }

            override fun onError(error: FacebookException) {
                onError(
                    error.localizedMessage
                        ?: "Error con Facebook."
                )
            }
        }

        LoginManager.getInstance()
            .registerCallback(callbackManager, callback)

        onDispose {
            LoginManager.getInstance()
                .unregisterCallback(callbackManager)
        }
    }

    AuthSecondaryButton(
        text = "Facebook",
        onClick = {

            val intent = LoginManager.getInstance()
                .logInWithReadPermissions(
                    activity,
                    listOf("email", "public_profile")
                )

        },
        modifier = Modifier.width(160.dp)
    )
}