package com.v1k70r.fitnessapp.ui.screens.pedometer

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.ui.screens.pedometer.components.BotonPausaPasos
import com.v1k70r.fitnessapp.ui.screens.pedometer.components.ResumenDiarioPasos
import com.v1k70r.fitnessapp.ui.screens.pedometer.components.ResumenMensualPasos
import com.v1k70r.fitnessapp.ui.screens.pedometer.components.ResumenSemanalPasos
import com.v1k70r.fitnessapp.ui.screens.pedometer.components.SelectorPeriodoPasos
import com.v1k70r.fitnessapp.ui.screens.pedometer.service.StepCounterService
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@Composable
fun PedometerScreen(
    viewModel: PedometerViewModel
){

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val activityRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(context, StepCounterService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val intent = Intent(context, StepCounterService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } else {
            activityRecognitionLauncher.launch(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        }
    }

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            contentPadding = PaddingValues(20.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {

                Text(
                    text = "Pasos",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {

                SelectorPeriodoPasos(
                    selectedPeriod = uiState.selectedPeriod,
                    onPeriodSelected = viewModel::changePeriod
                )
            }

            item {

                when (uiState.selectedPeriod) {

                    PedometerPeriod.DIA -> {

                        ResumenDiarioPasos(
                            steps = uiState.todaySteps,
                            goal = uiState.dailyGoal,
                            progress = uiState.progress,
                            calories = uiState.calories,
                            distanceKm = uiState.distanceKm
                        )
                    }

                    PedometerPeriod.SEMANA -> {

                        ResumenSemanalPasos(
                            totalSteps = uiState.weeklySteps,
                            averageSteps = uiState.weeklyAverage,
                            history = uiState.weeklyHistory
                        )
                    }

                    PedometerPeriod.MES -> {

                        ResumenMensualPasos(
                            totalSteps = uiState.monthlySteps,
                            averageSteps = uiState.monthlyAverage,
                            history = uiState.monthlyHistory
                        )
                    }
                }
            }

            item {

                BotonPausaPasos(
                    isPaused = uiState.isPaused,
                    onTogglePause = viewModel::togglePause
                )
            }
        }
    }
}