package com.v1k70r.fitnessapp.ui.screens.training.exerciselist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes

@Composable
fun ExerciseListScreen(
    navController: NavHostController,
    category: String
) {
    val exercises = when (category) {
        "Pecho" -> listOf(
            "Press banca",
            "Press inclinado con barra",
            "Press plano con mancuernas",
            "Aperturas en polea"
        )

        "Espalda" -> listOf(
            "Dominadas",
            "Remo con barra",
            "Jalón al pecho",
            "Remo sentado en polea"
        )

        "Piernas" -> listOf(
            "Sentadilla",
            "Prensa de piernas",
            "Peso muerto rumano",
            "Curl femoral"
        )

        "Hombros" -> listOf(
            "Press militar",
            "Elevaciones laterales",
            "Aperturas posteriores",
            "Press Arnold"
        )

        "Brazos" -> listOf(
            "Curl de bíceps",
            "Extensión de tríceps en polea",
            "Curl martillo",
            "Rompecráneos"
        )

        "Abdomen" -> listOf(
            "Crunch abdominal",
            "Plancha",
            "Elevación de piernas colgado",
            "Crunch en polea"
        )

        else -> emptyList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        contentPadding = PaddingValues(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = category,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(exercises) { exercise ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            TrainingRoutes.TrackExercise.createRoute(
                                category = category,
                                exerciseName = exercise
                            )
                        )
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = exercise,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                )
            }
        }
    }
}