package com.v1k70r.fitnessapp.ui.screens.training.workoutlog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import com.v1k70r.fitnessapp.ui.screens.training.workoutlog.components.SesionEntrenamientoCard

@Composable
fun WorkoutLogScreen(
    navController: NavHostController,
    trainingViewModel: TrainingViewModel
) {
    val sesionesEntrenamiento by trainingViewModel.sesionesEntrenamiento.collectAsState()
    var serieEditandoId by remember { mutableStateOf<Long?>(null) }
    var pesoEditado by remember { mutableStateOf("") }
    var repeticionesEditadas by remember { mutableStateOf("") }

    val sesionesPorDia = sesionesEntrenamiento.groupBy { session ->
        formatearFecha(session.startedAt)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        contentPadding = PaddingValues(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Registro de entrenamiento",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Button(
                onClick = {
                    navController.navigate(TrainingRoutes.Categories.route)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Agregar ejercicio")
            }
        }

        item {
            Button(
                onClick = {
                    trainingViewModel.finalizarSesionActiva()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Finalizar entrenamiento")
            }
        }

        sesionesPorDia.forEach { (fecha, sesionesDelDia) ->

            item {
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(sesionesDelDia, key = { it.id }) { sesion ->

                SesionEntrenamientoCard(
                    sesion = sesion,
                    serieEditandoId = serieEditandoId,
                    pesoEditado = pesoEditado,
                    repeticionesEditadas = repeticionesEditadas,
                    onPesoEditadoChange = { pesoEditado = it },
                    onRepeticionesEditadasChange = { repeticionesEditadas = it },
                    onEditarSerieClick = { serieId, peso, repeticiones ->
                        serieEditandoId = serieId
                        pesoEditado = peso
                        repeticionesEditadas = repeticiones
                    },
                    onGuardarSerieClick = { serieId, ejercicioRegistradoId, numeroSerie ->
                        trainingViewModel.actualizarSerieGuardada(
                            serieId = serieId,
                            ejercicioRegistradoId = ejercicioRegistradoId,
                            numeroSerie = numeroSerie,
                            peso = pesoEditado,
                            repeticiones = repeticionesEditadas
                        )

                        serieEditandoId = null
                        pesoEditado = ""
                        repeticionesEditadas = ""
                    },
                    onCancelarEdicionClick = {
                        serieEditandoId = null
                        pesoEditado = ""
                        repeticionesEditadas = ""
                    },
                    onBorrarSerieClick = { serieId ->
                        trainingViewModel.borrarSerieGuardada(serieId)

                        if (serieEditandoId == serieId) {
                            serieEditandoId = null
                            pesoEditado = ""
                            repeticionesEditadas = ""
                        }
                    },
                    onBorrarSesionClick = {
                        trainingViewModel.borrarSesionEntrenamiento(sesion.id)
                    },
                    onBorrarEjercicioClick = { ejercicioRegistradoId ->
                        trainingViewModel.borrarEjercicioRegistrado(ejercicioRegistradoId)
                    }
                )
            }
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .withLocale(java.util.Locale("es", "ES"))

    return java.time.Instant.ofEpochMilli(timestamp)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}