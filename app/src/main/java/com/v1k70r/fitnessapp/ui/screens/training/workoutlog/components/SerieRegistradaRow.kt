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
import androidx.compose.ui.Alignment
import com.v1k70r.fitnessapp.domain.model.ExerciseSet

@Composable
fun SerieRegistradaRow(
    serie: ExerciseSet,
    onEditarClick: () -> Unit,
    onBorrarClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Serie ${serie.setNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${serie.weight} kg x ${serie.reps} reps",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row {
            TextButton(
                onClick = onEditarClick
            ) {
                Text(
                    text = "Editar",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            TextButton(
                onClick = onBorrarClick
            ) {
                Text(
                    text = "Borrar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}