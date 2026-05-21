package com.v1k70r.fitnessapp.ui.screens.training.workoutlog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.domain.model.LoggedExercise

@Composable
fun EjercicioRegistradoItem(
    ejercicio: LoggedExercise,
    serieEditandoId: Long?,
    pesoEditado: String,
    repeticionesEditadas: String,
    onPesoEditadoChange: (String) -> Unit,
    onRepeticionesEditadasChange: (String) -> Unit,
    onEditarSerieClick: (serieId: Long, peso: String, repeticiones: String) -> Unit,
    onGuardarSerieClick: (serieId: Long, numeroSerie: Int) -> Unit,
    onCancelarEdicionClick: () -> Unit,
    onBorrarSerieClick: (serieId: Long) -> Unit,
    onBorrarEjercicioClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalDivider()

        Text(
            text = ejercicio.exerciseName,
            style = MaterialTheme.typography.titleSmall
        )

        ejercicio.sets.forEach { serie ->

            if (serieEditandoId == serie.id) {

                EditarSerieGuardadaForm(
                    peso = pesoEditado,
                    repeticiones = repeticionesEditadas,
                    onPesoChange = onPesoEditadoChange,
                    onRepeticionesChange = onRepeticionesEditadasChange,
                    onGuardarClick = {
                        onGuardarSerieClick(
                            serie.id,
                            serie.setNumber
                        )
                    },
                    onCancelarClick = onCancelarEdicionClick
                )

            } else {

                SerieRegistradaRow(
                    serie = serie,
                    onEditarClick = {
                        onEditarSerieClick(
                            serie.id,
                            serie.weight,
                            serie.reps
                        )
                    },
                    onBorrarClick = {
                        onBorrarSerieClick(serie.id)
                    }
                )
            }
        }

        Button(
            onClick = onBorrarEjercicioClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar ejercicio")
        }
    }
}