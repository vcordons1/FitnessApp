package com.v1k70r.fitnessapp.ui.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.v1k70r.fitnessapp.R
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthPrimaryButton
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthSecondaryButton
import com.v1k70r.fitnessapp.ui.screens.auth.components.AuthTextField
import com.v1k70r.fitnessapp.ui.screens.auth.components.SocialLoginButton

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthenticated: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val uiState by authViewModel.uiState.collectAsState()

    var authMode by remember { mutableStateOf(AuthMode.Login) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val googleSignInClient = remember {
        buildGoogleSignInClient(activity)
    }

    val googleLauncher = rememberLauncherForActivityResult(
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
        } else {
            authViewModel.mostrarError("Inicio de sesión con Google cancelado.")
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "F",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (authMode == AuthMode.Login) "Bienvenido de vuelta" else "Crea tu cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (authMode == AuthMode.Login) {
                        "Accede para continuar con tu entrenamiento, pasos y progreso."
                    } else {
                        "Regístrate para guardar tu información de forma segura."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AuthTextField(
                            value = email,
                            placeholder = "Correo electrónico",
                            onValueChange = { email = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AuthTextField(
                            value = password,
                            placeholder = "Contraseña",
                            onValueChange = { password = it },
                            isPassword = true
                        )

                        if (authMode == AuthMode.Register) {
                            Spacer(modifier = Modifier.height(12.dp))

                            AuthTextField(
                                value = confirmPassword,
                                placeholder = "Confirmar contraseña",
                                onValueChange = { confirmPassword = it },
                                isPassword = true
                            )
                        }

                        if (authMode == AuthMode.Login) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        AuthPrimaryButton(
                            text = if (authMode == AuthMode.Login) "Iniciar sesión" else "Crear cuenta",
                            isLoading = uiState.isLoading,
                            onClick = {
                                if (authMode == AuthMode.Login) {
                                    authViewModel.iniciarSesionConEmail(email, password)
                                } else {
                                    authViewModel.crearCuentaConEmail(
                                        email = email,
                                        password = password,
                                        confirmPassword = confirmPassword
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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
                                authMode = if (authMode == AuthMode.Login) {
                                    AuthMode.Register
                                } else {
                                    AuthMode.Login
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f))

                            Text(
                                text = "  O continúa con  ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            HorizontalDivider(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SocialLoginButton(
                                text = "G",
                                onClick = {
                                    googleSignInClient.signOut()
                                    googleLauncher.launch(googleSignInClient.signInIntent)
                                },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            FacebookLoginButton(
                                onTokenReceived = { token ->
                                    authViewModel.iniciarSesionConFacebook(token)
                                },
                                onError = { error ->
                                    authViewModel.mostrarError(error)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        uiState.errorMessage?.let { message ->
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "FitnessApp",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun buildGoogleSignInClient(
    activity: Activity
): GoogleSignInClient {
    val options = GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(activity, options)
}

@Composable
private fun FacebookLoginButton(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as Activity

    val callbackManager = remember {
        CallbackManager.Factory.create()
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
                onError(error.localizedMessage ?: "Error con Facebook.")
            }
        }

        LoginManager.getInstance().registerCallback(callbackManager, callback)

        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

    AuthSecondaryButton(
        text = "F",
        onClick = {
            LoginManager.getInstance().logInWithReadPermissions(
                activity,
                listOf("email", "public_profile")
            )
        },
        modifier = modifier
    )
}