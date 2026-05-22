package com.v1k70r.fitnessapp.ui.screens.pedometer.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BotonPausaPasos(
    isPaused: Boolean,
    onTogglePause: () -> Unit
) {

    Button(
        onClick = onTogglePause,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = if (isPaused) {
                "Reanudar conteo"
            } else {
                "Pausar conteo"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}