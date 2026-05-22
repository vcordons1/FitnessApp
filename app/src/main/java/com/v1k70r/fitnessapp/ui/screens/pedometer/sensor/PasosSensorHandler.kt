package com.v1k70r.fitnessapp.ui.screens.pedometer.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun PasosSensorHandler(
    activo: Boolean,
    onPasosDetectados: (Int) -> Unit,
    onSensorDisponible: (Boolean) -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(activo) {
        if (!activo) {
            onDispose { }
        } else {
            val sensorManager =
                context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            val stepSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            if (stepSensor == null) {
                onSensorDisponible(false)
                onDispose { }
            } else {
                onSensorDisponible(true)

                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        val pasos = event?.values?.firstOrNull()?.toInt() ?: return
                        onPasosDetectados(pasos)
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
                }

                sensorManager.registerListener(
                    listener,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_UI
                )

                onDispose {
                    sensorManager.unregisterListener(listener)
                }
            }
        }
    }
}