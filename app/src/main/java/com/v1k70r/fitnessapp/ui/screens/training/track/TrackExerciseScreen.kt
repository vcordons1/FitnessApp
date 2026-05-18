package com.v1k70r.fitnessapp.ui.screens.training.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import com.v1k70r.fitnessapp.domain.model.ExerciseSet



@Composable
fun TrackExerciseScreen(
    navController: NavHostController,
    exerciseName: String,
    trainingViewModel: TrainingViewModel
) {
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }

    val sets = remember {
        mutableStateListOf<ExerciseSet>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = {
                    Text("Peso")
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = reps,
                onValueChange = { reps = it },
                label = {
                    Text("Reps")
                },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                if (weight.isNotBlank() && reps.isNotBlank()) {

                    sets.add(
                        ExerciseSet(
                            weight = weight,
                            reps = reps
                        )
                    )

                    weight = ""
                    reps = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar set")
        }

        Button(
            onClick = {
                trainingViewModel.addLoggedExercise(
                    exerciseName = exerciseName,
                    sets = sets.toList()
                )

                navController.popBackStack(
                    route = TrainingRoutes.WorkoutLog.route,
                    inclusive = false
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = sets.isNotEmpty()
        ) {
            Text("Guardar ejercicio")
        }

        Text(
            text = "Sets registrados: ${sets.size}",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sets) { set ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "${set.weight} kg x ${set.reps} reps",
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}