package com.v1k70r.fitnessapp.ui.screens.pedometer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.domain.model.DailyStepRecord

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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumen semanal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { day ->
                    TarjetaMetricaPasos(
                        title = day.date.takeLast(2),
                        value = "${day.steps}",
                        modifier = Modifier.widthIn(min = 70.dp)
                    )
                }
            }
        }
    }
}