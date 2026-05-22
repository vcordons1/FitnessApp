package com.v1k70r.fitnessapp.ui.screens.pedometer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v1k70r.fitnessapp.ui.screens.pedometer.PedometerPeriod

@Composable
fun SelectorPeriodoPasos(
    selectedPeriod: PedometerPeriod,
    onPeriodSelected: (PedometerPeriod) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),

        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        SelectorItem(
            texto = "D",
            seleccionado = selectedPeriod == PedometerPeriod.DIA,
            onClick = {
                onPeriodSelected(PedometerPeriod.DIA)
            },
            modifier = Modifier.weight(1f)
        )

        SelectorItem(
            texto = "S",
            seleccionado = selectedPeriod == PedometerPeriod.SEMANA,
            onClick = {
                onPeriodSelected(PedometerPeriod.SEMANA)
            },
            modifier = Modifier.weight(1f)
        )

        SelectorItem(
            texto = "M",
            seleccionado = selectedPeriod == PedometerPeriod.MES,
            onClick = {
                onPeriodSelected(PedometerPeriod.MES)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SelectorItem(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (seleccionado) {
                    MaterialTheme.colorScheme.surface
                } else {
                    androidx.compose.ui.graphics.Color.Transparent
                }
            )
            .clickable {
                onClick()
            },

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = texto,

            fontSize = 20.sp,

            fontWeight = FontWeight.Bold,

            color =
                if (seleccionado) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
        )
    }
}