package com.v1k70r.fitnessapp.ui.screens.pedometer

import com.v1k70r.fitnessapp.ui.screens.pedometer.data.DailyStepRecord

data class PedometerUiState(
    val selectedPeriod: PedometerPeriod = PedometerPeriod.DIA,
    val todaySteps: Int = 0,
    val dailyGoal: Int = 10000,
    val progress: Float = 0f,
    val calories: Int = 0,
    val distanceKm: Double = 0.0,
    val weeklySteps: Int = 0,
    val weeklyAverage: Int = 0,
    val monthlySteps: Int = 0,
    val monthlyAverage: Int = 0,
    val weeklyHistory: List<DailyStepRecord> = emptyList(),
    val monthlyHistory: List<DailyStepRecord> = emptyList(),

    val isPaused: Boolean = false,

    val pauseStartSensorSteps: Int? = null,
    val pausedStepsOffset: Int = 0
)