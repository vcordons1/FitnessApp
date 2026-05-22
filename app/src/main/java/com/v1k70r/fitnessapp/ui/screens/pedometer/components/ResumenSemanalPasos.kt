package com.v1k70r.fitnessapp.ui.screens.pedometer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.ui.screens.pedometer.data.DailyStepRecord

@Composable
fun ResumenSemanalPasos(
    totalSteps: Int,
    averageSteps: Int,
    history: List<DailyStepRecord>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Resumen semanal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaMetricaPasos(
                    title = "Total",
                    value = "$totalSteps",
                    modifier = Modifier.weight(1f)
                )

                TarjetaMetricaPasos(
                    title = "Promedio",
                    value = "$averageSteps",
                    modifier = Modifier.weight(1f)
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history) { day ->
                    TarjetaMetricaPasos(
                        title = day.date.takeLast(2),
                        value = "${day.steps}",
                        modifier = Modifier.width(92.dp)
                    )
                }
            }
        }
    }
}