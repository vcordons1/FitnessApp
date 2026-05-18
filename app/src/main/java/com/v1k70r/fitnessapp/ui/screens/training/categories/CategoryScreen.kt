package com.v1k70r.fitnessapp.ui.screens.training.categories

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
fun CategoryScreen(
    navController: NavHostController
) {
    val categories = listOf(
        "Chest",
        "Back",
        "Legs",
        "Shoulders",
        "Arms",
        "Core"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                TrainingRoutes.ExerciseList.createRoute(category)
                            )
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }
}