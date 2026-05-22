package com.v1k70r.fitnessapp.ui.screens.pedometer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.ui.screens.pedometer.data.DailyStepRecord
import com.v1k70r.fitnessapp.ui.screens.pedometer.data.StepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class PedometerViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = FitnessDatabase.getDatabase(application)

    private val repository = StepRepository(
        stepDao = database.stepDao()
    )

    private val _uiState = MutableStateFlow(PedometerUiState())
    val uiState: StateFlow<PedometerUiState> = _uiState.asStateFlow()

    private var baseSensorSteps: Int? = null
    private var lastSensorSteps: Int = 0
    private var activeDate: String = LocalDate.now().toString()

    private val dailyGoal = 10000

    init {
        loadStoredSteps()
        observeHistory()
    }

    fun onSensorStepsChanged(sensorSteps: Int) {
        if (_uiState.value.isPaused) {
            lastSensorSteps = sensorSteps
            return
        }

        viewModelScope.launch {
            val today = LocalDate.now().toString()

            if (activeDate != today) {
                activeDate = today
                baseSensorSteps = sensorSteps
                lastSensorSteps = sensorSteps

                updateStateWithTodaySteps(0)

                repository.saveTodaySteps(
                    steps = 0,
                    baseSensorSteps = sensorSteps,
                    lastSensorSteps = sensorSteps,
                    goal = dailyGoal,
                    isPaused = false,
                    pauseStartSensorSteps = null,
                    pausedStepsOffset = 0
                )

                return@launch
            }

            val storedToday = repository.getTodaySteps()

            if (storedToday == null) {
                baseSensorSteps = sensorSteps
                lastSensorSteps = sensorSteps

                _uiState.value = _uiState.value.copy(
                    isPaused = false,
                    pauseStartSensorSteps = null,
                    pausedStepsOffset = 0
                )

                updateStateWithTodaySteps(0)

                repository.saveTodaySteps(
                    steps = 0,
                    baseSensorSteps = sensorSteps,
                    lastSensorSteps = sensorSteps,
                    goal = dailyGoal,
                    isPaused = false,
                    pauseStartSensorSteps = null,
                    pausedStepsOffset = 0
                )

                return@launch
            }

            if (baseSensorSteps == null) {
                baseSensorSteps = storedToday.baseSensorSteps
                lastSensorSteps = storedToday.lastSensorSteps

                _uiState.value = _uiState.value.copy(
                    isPaused = storedToday.isPaused,
                    pauseStartSensorSteps = storedToday.pauseStartSensorSteps,
                    pausedStepsOffset = storedToday.pausedStepsOffset
                )
            }

            lastSensorSteps = sensorSteps

            val base = baseSensorSteps ?: sensorSteps
            val offset = _uiState.value.pausedStepsOffset

            val currentSteps =
                (sensorSteps - base - offset).coerceAtLeast(0)

            updateStateWithTodaySteps(currentSteps)

            repository.saveTodaySteps(
                steps = currentSteps,
                baseSensorSteps = base,
                lastSensorSteps = sensorSteps,
                goal = dailyGoal,
                isPaused = _uiState.value.isPaused,
                pauseStartSensorSteps = _uiState.value.pauseStartSensorSteps,
                pausedStepsOffset = _uiState.value.pausedStepsOffset
            )
        }
    }

    fun togglePause() {
        val currentState = _uiState.value

        viewModelScope.launch {
            val storedToday = repository.getTodaySteps()

            val base = storedToday?.baseSensorSteps ?: baseSensorSteps ?: lastSensorSteps

            if (!currentState.isPaused) {
                _uiState.value = currentState.copy(
                    isPaused = true,
                    pauseStartSensorSteps = lastSensorSteps
                )

                repository.saveTodaySteps(
                    steps = currentState.todaySteps,
                    baseSensorSteps = base,
                    lastSensorSteps = lastSensorSteps,
                    goal = dailyGoal,
                    isPaused = true,
                    pauseStartSensorSteps = lastSensorSteps,
                    pausedStepsOffset = currentState.pausedStepsOffset
                )
            } else {
                val pauseStart = currentState.pauseStartSensorSteps ?: lastSensorSteps

                val walkedWhilePaused =
                    (lastSensorSteps - pauseStart).coerceAtLeast(0)

                val newOffset =
                    currentState.pausedStepsOffset + walkedWhilePaused

                _uiState.value = currentState.copy(
                    isPaused = false,
                    pauseStartSensorSteps = null,
                    pausedStepsOffset = newOffset
                )

                repository.saveTodaySteps(
                    steps = currentState.todaySteps,
                    baseSensorSteps = base,
                    lastSensorSteps = lastSensorSteps,
                    goal = dailyGoal,
                    isPaused = false,
                    pauseStartSensorSteps = null,
                    pausedStepsOffset = newOffset
                )
            }
        }
    }

    fun changePeriod(period: PedometerPeriod) {
        _uiState.value = _uiState.value.copy(
            selectedPeriod = period
        )
    }

    private fun loadStoredSteps() {
        viewModelScope.launch {
            activeDate = LocalDate.now().toString()

            val todaySteps = repository.getTodaySteps()

            if (todaySteps != null) {
                baseSensorSteps = todaySteps.baseSensorSteps
                lastSensorSteps = todaySteps.lastSensorSteps

                _uiState.value = _uiState.value.copy(
                    isPaused = todaySteps.isPaused,
                    pauseStartSensorSteps = todaySteps.pauseStartSensorSteps,
                    pausedStepsOffset = todaySteps.pausedStepsOffset
                )

                updateStateWithTodaySteps(todaySteps.steps)
            }
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            combine(
                repository.observeCurrentWeekSteps(),
                repository.observeCurrentMonthSteps()
            ) { weekRecords, monthRecords ->

                val weeklySteps = weekRecords.sumOf { it.steps }
                val monthlySteps = monthRecords.sumOf { it.steps }

                val weeklyAverage = if (weekRecords.isNotEmpty()) {
                    weeklySteps / weekRecords.size
                } else {
                    0
                }

                val monthlyAverage = if (monthRecords.isNotEmpty()) {
                    monthlySteps / monthRecords.size
                } else {
                    0
                }

                _uiState.value = _uiState.value.copy(
                    weeklySteps = weeklySteps,
                    weeklyAverage = weeklyAverage,
                    monthlySteps = monthlySteps,
                    monthlyAverage = monthlyAverage,
                    weeklyHistory = weekRecords.map {
                        DailyStepRecord(
                            date = it.date,
                            steps = it.steps,
                            goal = it.goal
                        )
                    },
                    monthlyHistory = monthRecords.map {
                        DailyStepRecord(
                            date = it.date,
                            steps = it.steps,
                            goal = it.goal
                        )
                    }
                )
            }.collect {}
        }
    }

    private fun updateStateWithTodaySteps(steps: Int) {
        val distanceKm = steps * 0.0008
        val calories = (steps * 0.04).toInt()
        val progress = (steps.toFloat() / dailyGoal).coerceIn(0f, 1f)

        _uiState.value = _uiState.value.copy(
            todaySteps = steps,
            dailyGoal = dailyGoal,
            distanceKm = distanceKm,
            calories = calories,
            progress = progress
        )
    }
}