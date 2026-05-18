package com.v1k70r.fitnessapp.ui.screens.training.exerciselist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes

@Composable
fun ExerciseListScreen(
    navController: NavHostController,
    category: String
) {
    val exercises = when (category) {
        "Chest" -> listOf(
            "Bench Press",
            "Incline Barbell Bench Press",
            "Flat Dumbbell Bench Press",
            "Cable Fly"
        )

        "Back" -> listOf(
            "Pull Ups",
            "Barbell Row",
            "Lat Pulldown",
            "Seated Cable Row"
        )

        "Legs" -> listOf(
            "Squat",
            "Leg Press",
            "Romanian Deadlift",
            "Leg Curl"
        )

        "Shoulders" -> listOf(
            "Overhead Press",
            "Lateral Raise",
            "Rear Delt Fly",
            "Arnold Press"
        )

        "Arms" -> listOf(
            "Biceps Curl",
            "Triceps Pushdown",
            "Hammer Curl",
            "Skull Crushers"
        )

        "Core" -> listOf(
            "Crunches",
            "Plank",
            "Hanging Leg Raise",
            "Cable Crunch"
        )

        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                TrainingRoutes.TrackExercise.createRoute(exercise)
                            )
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = exercise,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }
}