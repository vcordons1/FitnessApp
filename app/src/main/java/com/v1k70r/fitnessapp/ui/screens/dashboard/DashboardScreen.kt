package com.v1k70r.fitnessapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.ui.components.StatCard

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "FitnessApp",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Resumen diario",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Calorías",
                value = "1,840 kcal",
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Pasos",
                value = "7,230",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Entrenos",
                value = "1 sesión",
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Proteína",
                value = "120 g",
                modifier = Modifier.weight(1f)
            )
        }
    }
}