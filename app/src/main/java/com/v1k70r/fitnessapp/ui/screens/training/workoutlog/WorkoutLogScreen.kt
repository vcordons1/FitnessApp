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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card

@Composable
fun WorkoutLogScreen(
    navController: NavHostController,
    trainingViewModel: TrainingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Workout Log",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Aquí se mostrarán los entrenamientos del día.",
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

        LazyColumn {

            items(trainingViewModel.loggedExercises) { loggedExercise ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = loggedExercise.exerciseName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        loggedExercise.sets.forEach { set ->

                            Text(
                                text = "${set.weight} kg x ${set.reps} reps"
                            )
                        }
                    }
                }
            }
        }
    }
}