package com.v1k70r.fitnessapp.ui.screens.training.workoutlog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditarSerieGuardadaForm(
    peso: String,
    repeticiones: String,
    onPesoChange: (String) -> Unit,
    onRepeticionesChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onCancelarClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = peso,
            onValueChange = onPesoChange,
            label = { Text("Peso") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = repeticiones,
            onValueChange = onRepeticionesChange,
            label = { Text("Repeticiones") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }

        Button(
            onClick = onCancelarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}