package com.v1k70r.fitnessapp.ui.screens.pedometer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun ResumenMensualPasos(
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
                text = "Resumen mensual",
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

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 360.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(history) { day ->
                    TarjetaMetricaPasos(
                        title = day.date.takeLast(2),
                        value = "${day.steps}"
                    )
                }
            }
        }
    }
}