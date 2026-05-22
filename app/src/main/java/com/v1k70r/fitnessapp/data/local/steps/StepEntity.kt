package com.v1k70r.fitnessapp.data.local.steps

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class StepEntity(
    @PrimaryKey val date: String,
    val steps: Int,
    val baseSensorSteps: Int,
    val lastSensorSteps: Int,
    val goal: Int = 10000,
    val isPaused: Boolean = false,
    val pauseStartSensorSteps: Int? = null,
    val pausedStepsOffset: Int = 0
)