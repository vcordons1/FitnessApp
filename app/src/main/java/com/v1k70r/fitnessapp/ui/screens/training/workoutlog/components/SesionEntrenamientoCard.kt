package com.v1k70r.fitnessapp.ui.screens.training.workoutlog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.domain.model.WorkoutSession

@Composable
fun SesionEntrenamientoCard(
    sesion: WorkoutSession,
    serieEditandoId: Long?,
    pesoEditado: String,
    repeticionesEditadas: String,
    onPesoEditadoChange: (String) -> Unit,
    onRepeticionesEditadasChange: (String) -> Unit,
    onEditarSerieClick: (serieId: Long, peso: String, repeticiones: String) -> Unit,
    onGuardarSerieClick: (
        serieId: Long,
        ejercicioRegistradoId: Long,
        numeroSerie: Int
    ) -> Unit,
    onCancelarEdicionClick: () -> Unit,
    onBorrarSesionClick: () -> Unit,
    onBorrarSerieClick: (serieId: Long) -> Unit,
    onBorrarEjercicioClick: (ejercicioRegistradoId: Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = sesion.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (sesion.endedAt == null) {
                    "Estado: En progreso"
                } else {
                    "Estado: Finalizado"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            sesion.exercises.forEach { ejercicio ->
                EjercicioRegistradoItem(
                    ejercicio = ejercicio,
                    serieEditandoId = serieEditandoId,
                    pesoEditado = pesoEditado,
                    repeticionesEditadas = repeticionesEditadas,
                    onPesoEditadoChange = onPesoEditadoChange,
                    onRepeticionesEditadasChange = onRepeticionesEditadasChange,
                    onEditarSerieClick = onEditarSerieClick,
                    onGuardarSerieClick = { serieId, numeroSerie ->
                        onGuardarSerieClick(
                            serieId,
                            ejercicio.id,
                            numeroSerie
                        )
                    },
                    onCancelarEdicionClick = onCancelarEdicionClick,
                    onBorrarSerieClick = onBorrarSerieClick,
                    onBorrarEjercicioClick = {
                        onBorrarEjercicioClick(ejercicio.id)
                    }
                )
            }

            TextButton(
                onClick = onBorrarSesionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Borrar entrenamiento",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}