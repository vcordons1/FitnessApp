package com.v1k70r.fitnessapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.ui.components.StatCard
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun DashboardScreen(
    trainingViewModel: TrainingViewModel
) {

    val sesionesEntrenamiento by trainingViewModel.sesionesEntrenamiento.collectAsState()

    val sesionActivaHoy = sesionesEntrenamiento.firstOrNull { session ->
        session.endedAt == null && esHoy(session.startedAt)
    }

    val ejerciciosRegistrados = sesionActivaHoy?.exercises?.size ?: 0

    val seriesTotales = sesionActivaHoy
        ?.exercises
        ?.sumOf { exercise -> exercise.sets.size }
        ?: 0

    val ultimoEjercicio = sesionActivaHoy
        ?.exercises
        ?.lastOrNull()
        ?.exerciseName
        ?: "Sin ejercicios registrados"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Entrenamiento de hoy",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = if (sesionActivaHoy == null) {
                "No hay entrenamiento activo"
            } else {
                "Entrenamiento en progreso"
            }
        )

        Text(
            text = "Ejercicios registrados: $ejerciciosRegistrados"
        )

        Text(
            text = "Series totales: $seriesTotales"
        )

        Text(
            text = "Último ejercicio: $ultimoEjercicio"
        )
    }
}

private fun esHoy(timestamp: Long): Boolean {
    val calendarioSesion = java.util.Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    val calendarioHoy = java.util.Calendar.getInstance()

    return calendarioSesion.get(java.util.Calendar.YEAR) == calendarioHoy.get(java.util.Calendar.YEAR) &&
            calendarioSesion.get(java.util.Calendar.DAY_OF_YEAR) == calendarioHoy.get(java.util.Calendar.DAY_OF_YEAR)
}