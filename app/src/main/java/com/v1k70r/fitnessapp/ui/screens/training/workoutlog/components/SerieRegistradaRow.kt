package com.v1k70r.fitnessapp.ui.screens.training.workoutlog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.v1k70r.fitnessapp.domain.model.ExerciseSet

@Composable
fun SerieRegistradaRow(
    serie: ExerciseSet,
    onEditarClick: () -> Unit,
    onBorrarClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Serie ${serie.setNumber}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${serie.weight} kg x ${serie.reps} reps",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row {
            TextButton(
                onClick = onEditarClick
            ) {
                Text("Editar")
            }

            TextButton(
                onClick = onBorrarClick
            ) {
                Text("Borrar")
            }
        }
    }
}