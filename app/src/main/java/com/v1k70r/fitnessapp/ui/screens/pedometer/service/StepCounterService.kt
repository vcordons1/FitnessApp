package com.v1k70r.fitnessapp.ui.screens.pedometer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.v1k70r.fitnessapp.MainActivity
import com.v1k70r.fitnessapp.R
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.ui.screens.pedometer.data.StepRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate

class StepCounterService : Service(), SensorEventListener {

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null

    private lateinit var repository: StepRepository

    private var baseSensorSteps: Int? = null
    private var activeDate: String = LocalDate.now().toString()

    override fun onCreate() {
        super.onCreate()

        val database = FitnessDatabase.getDatabase(applicationContext)

        repository = StepRepository(
            stepDao = database.stepDao()
        )

        sensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepCounterSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            startForeground(
                NOTIFICATION_ID,
                buildNotification(0),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )

        } else {

            startForeground(
                NOTIFICATION_ID,
                buildNotification(0)
            )
        }

        startStepCounter()

        return START_STICKY
    }

    private fun startStepCounter() {
        stepCounterSensor?.let { sensor ->

            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type != Sensor.TYPE_STEP_COUNTER) return

        val sensorSteps =
            event.values.firstOrNull()?.toInt() ?: return

        serviceScope.launch {

            val today = LocalDate.now().toString()

            if (activeDate != today) {
                activeDate = today
                baseSensorSteps = sensorSteps
            }

            val storedToday = repository.getTodaySteps()

            if (baseSensorSteps == null) {
                baseSensorSteps =
                    storedToday?.baseSensorSteps ?: sensorSteps
            }

            if (storedToday?.isPaused == true) {

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE)
                            as NotificationManager

                notificationManager.notify(
                    NOTIFICATION_ID,
                    buildNotification(storedToday.steps)
                )

                return@launch
            }

            val base = baseSensorSteps ?: sensorSteps

            val pausedOffset =
                storedToday?.pausedStepsOffset ?: 0

            val currentSteps =
                (sensorSteps - base - pausedOffset)
                    .coerceAtLeast(0)

            repository.saveTodaySteps(
                steps = currentSteps,
                baseSensorSteps = base,
                lastSensorSteps = sensorSteps,
                goal = storedToday?.goal ?: 10000,
                isPaused = storedToday?.isPaused ?: false,
                pauseStartSensorSteps =
                    storedToday?.pauseStartSensorSteps,
                pausedStepsOffset = pausedOffset
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager

            notificationManager.notify(
                NOTIFICATION_ID,
                buildNotification(currentSteps)
            )
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) = Unit

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(steps: Int): Notification {

        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or
                    PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Contador de pasos activo")
            .setContentText("Pasos de hoy: $steps")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Conteo de pasos",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID =
            "step_counter_channel"

        private const val NOTIFICATION_ID = 1001
    }
}