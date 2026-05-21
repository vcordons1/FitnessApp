package com.v1k70r.fitnessapp.ui.screens.training.workoutlog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextButton
import androidx.compose.material3.HorizontalDivider
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Registro de entrenamiento",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Aquí se mostrarán tus entrenamientos registrados",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = {
                navController.navigate(TrainingRoutes.Categories.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Agregar ejercicio")
        }

        Button(
            onClick = {
                trainingViewModel.finalizarSesionActiva()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar entrenamiento")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sesionesPorDia.forEach { (fecha, sesionesDelDia) ->

                item {
                    Text(
                        text = fecha,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(sesionesDelDia) { sesion ->

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
}

private fun formatearFecha(timestamp: Long): String {
    val formatter = java.text.SimpleDateFormat(
        "dd/MM/yyyy",
        java.util.Locale("es", "ES")
    )

    return formatter.format(java.util.Date(timestamp))
}