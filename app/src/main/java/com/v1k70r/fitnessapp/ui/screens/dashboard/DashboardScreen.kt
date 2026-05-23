package com.v1k70r.fitnessapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.ui.screens.pedometer.PedometerViewModel
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import java.util.Calendar

@Composable
fun DashboardScreen(
    trainingViewModel: TrainingViewModel,
    pedometerViewModel: PedometerViewModel
) {
    val sesionesEntrenamiento by trainingViewModel.sesionesEntrenamiento.collectAsState()
    val pasosState by pedometerViewModel.uiState.collectAsState()

    val sesionActivaHoy = sesionesEntrenamiento.firstOrNull { session ->
        session.endedAt == null && esHoy(session.startedAt)
    }

    val ejerciciosRegistrados = sesionActivaHoy?.exercises?.size ?: 0
    val seriesTotales = sesionActivaHoy?.exercises?.sumOf { it.sets.size } ?: 0
    val ultimoEjercicio = sesionActivaHoy?.exercises?.lastOrNull()?.exerciseName
        ?: "Sin ejercicios registrados"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Inicio",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        DashboardPasosCard(
            pasos = pasosState.todaySteps,
            meta = pasosState.dailyGoal,
            progreso = pasosState.progress,
            calorias = pasosState.calories,
            distanciaKm = pasosState.distanceKm,
            pausado = pasosState.isPaused
        )

        DashboardEntrenoCard(
            entrenamientoActivo = sesionActivaHoy != null,
            ejerciciosRegistrados = ejerciciosRegistrados,
            seriesTotales = seriesTotales,
            ultimoEjercicio = ultimoEjercicio
        )
    }
}

@Composable
private fun DashboardPasosCard(
    pasos: Int,
    meta: Int,
    progreso: Float,
    calorias: Int,
    distanciaKm: Double,
    pausado: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Pasos de hoy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$pasos",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${(progreso * 100).toInt()}% de la meta diaria ($meta pasos)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Calorías",
                    valor = "$calorias kcal"
                )

                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Distancia",
                    valor = "%.2f km".format(distanciaKm)
                )
            }

            Text(
                text = if (pausado) "Conteo pausado" else "Conteo activo",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DashboardEntrenoCard(
    entrenamientoActivo: Boolean,
    ejerciciosRegistrados: Int,
    seriesTotales: Int,
    ultimoEjercicio: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Entrenamiento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (entrenamientoActivo) {
                    "Entrenamiento activo hoy"
                } else {
                    "No hay entrenamiento activo"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Ejercicios",
                    valor = ejerciciosRegistrados.toString()
                )

                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Series",
                    valor = seriesTotales.toString()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Último ejercicio",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = ultimoEjercicio,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DashboardMiniCard(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun esHoy(timestamp: Long): Boolean {
    val calendarioSesion = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    val calendarioHoy = Calendar.getInstance()

    return calendarioSesion.get(Calendar.YEAR) == calendarioHoy.get(Calendar.YEAR) &&
            calendarioSesion.get(Calendar.DAY_OF_YEAR) == calendarioHoy.get(Calendar.DAY_OF_YEAR)
}