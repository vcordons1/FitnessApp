package com.v1k70r.fitnessapp.ui.screens.nutrition.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun MacroRing(
    progress: Float,
    modifier: Modifier = Modifier,
    ringDp: Int = 120,
    content: @Composable () -> Unit
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary
    val ringSize = ringDp.dp

    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(ringSize)) {
            val stroke = 16.dp.toPx()
            val canvasSize = this.size

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                size = canvasSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                size = canvasSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        content()
    }
}