package com.v1k70r.fitnessapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.v1k70r.fitnessapp.ui.screens.nutrition.NutritionViewModel
import com.v1k70r.fitnessapp.ui.screens.pedometer.PedometerViewModel
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

@Composable
fun DashboardScreen(
    trainingViewModel: TrainingViewModel,
    pedometerViewModel: PedometerViewModel,
    nutritionViewModel: NutritionViewModel
) {
    val sesionesEntrenamiento by trainingViewModel.sesionesEntrenamiento.collectAsState()
    val pasosState by pedometerViewModel.uiState.collectAsState()
    val nutritionState by nutritionViewModel.uiState.collectAsState()

    val sesionActivaHoy = sesionesEntrenamiento.firstOrNull { session ->
        session.endedAt == null && esHoy(session.startedAt)
    }

    val ejerciciosRegistrados = sesionActivaHoy?.exercises?.size ?: 0
    val seriesTotales = sesionActivaHoy?.exercises?.sumOf { it.sets.size } ?: 0

    val ultimoEjercicio = sesionActivaHoy
        ?.exercises
        ?.lastOrNull()
        ?.exerciseName
        ?: "Sin ejercicios registrados"

    val caloriasConsumidas = nutritionState.entries.sumOf { entry ->
        (entry.food.caloriesPer100g * entry.grams) / 100
    }

    val proteina = nutritionState.entries.sumOf { entry ->
        (entry.food.proteinPer100g * entry.grams) / 100.0
    }

    val carbohidratos = nutritionState.entries.sumOf { entry ->
        (entry.food.carbsPer100g * entry.grams) / 100.0
    }

    val grasas = nutritionState.entries.sumOf { entry ->
        (entry.food.fatsPer100g * entry.grams) / 100.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        DashboardNutricionCard(
            calorias = caloriasConsumidas,
            proteina = proteina,
            carbohidratos = carbohidratos,
            grasas = grasas
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Pasos de hoy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$pasos",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${(progreso * 100).toInt()}% de la meta diaria ($meta pasos)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DashboardNutricionCard(
    calorias: Int,
    proteina: Double,
    carbohidratos: Double,
    grasas: Double
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Nutrición",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$calorias kcal consumidas hoy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Proteína",
                    valor = "%.1f g".format(proteina)
                )

                DashboardMiniCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Carbs",
                    valor = "%.1f g".format(carbohidratos)
                )
            }

            DashboardMiniCard(
                modifier = Modifier.fillMaxWidth(),
                titulo = "Grasas",
                valor = "%.1f g".format(grasas)
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Entrenamiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (entrenamientoActivo) {
                    "Entrenamiento activo hoy"
                } else {
                    "No hay entrenamiento activo"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Último ejercicio",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = ultimoEjercicio,
                    style = MaterialTheme.typography.bodyLarge,
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
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun esHoy(timestamp: Long): Boolean {
    val fechaSesion = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return fechaSesion == LocalDate.now()
}