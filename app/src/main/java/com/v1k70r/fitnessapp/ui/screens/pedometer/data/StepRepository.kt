package com.v1k70r.fitnessapp.ui.screens.pedometer.data

import com.v1k70r.fitnessapp.data.local.steps.StepDao
import com.v1k70r.fitnessapp.data.local.steps.StepEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StepRepository(
    private val stepDao: StepDao
) {

    private val formatter = DateTimeFormatter.ISO_DATE

    private fun today(): String {
        return LocalDate.now().format(formatter)
    }

    suspend fun saveTodaySteps(
        steps: Int,
        baseSensorSteps: Int,
        lastSensorSteps: Int,
        goal: Int = 10000,
        isPaused: Boolean = false,
        pauseStartSensorSteps: Int? = null,
        pausedStepsOffset: Int = 0
    ) {
        withContext(Dispatchers.IO) {
            stepDao.upsertDailySteps(
                StepEntity(
                    date = today(),
                    steps = steps,
                    baseSensorSteps = baseSensorSteps,
                    lastSensorSteps = lastSensorSteps,
                    goal = goal,
                    isPaused = isPaused,
                    pauseStartSensorSteps = pauseStartSensorSteps,
                    pausedStepsOffset = pausedStepsOffset
                )
            )
        }
    }

    suspend fun getTodaySteps(): StepEntity? {
        return withContext(Dispatchers.IO) {
            stepDao.getStepsByDate(today())
        }
    }

    suspend fun getCurrentWeekSteps(): List<StepEntity> {
        return withContext(Dispatchers.IO) {
            buildCurrentWeekSteps(
                stepDao.getStepsBetween(
                    getStartOfCurrentWeek(),
                    today()
                )
            )
        }
    }

    suspend fun getCurrentMonthSteps(): List<StepEntity> {
        return withContext(Dispatchers.IO) {
            buildCurrentMonthSteps(
                stepDao.getStepsBetween(
                    getStartOfCurrentMonth(),
                    getEndOfCurrentMonth()
                )
            )
        }
    }

    fun observeCurrentWeekSteps(): Flow<List<StepEntity>> {
        return stepDao.observeStepsBetween(
            getStartOfCurrentWeek(),
            today()
        ).map { savedRecords ->
            buildCurrentWeekSteps(savedRecords)
        }
    }

    fun observeCurrentMonthSteps(): Flow<List<StepEntity>> {
        return stepDao.observeStepsBetween(
            getStartOfCurrentMonth(),
            getEndOfCurrentMonth()
        ).map { savedRecords ->
            buildCurrentMonthSteps(savedRecords)
        }
    }

    private fun buildCurrentWeekSteps(
        savedRecords: List<StepEntity>
    ): List<StepEntity> {
        val startOfWeek = LocalDate.parse(getStartOfCurrentWeek(), formatter)
        val recordsByDate = savedRecords.associateBy { it.date }

        return (0..6).map { index ->
            val date = startOfWeek.plusDays(index.toLong()).format(formatter)

            recordsByDate[date] ?: StepEntity(
                date = date,
                steps = 0,
                baseSensorSteps = 0,
                lastSensorSteps = 0,
                goal = 10000,
                isPaused = false,
                pauseStartSensorSteps = null,
                pausedStepsOffset = 0
            )
        }
    }

    private fun buildCurrentMonthSteps(
        savedRecords: List<StepEntity>
    ): List<StepEntity> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val recordsByDate = savedRecords.associateBy { it.date }

        return (1..today.lengthOfMonth()).map { day ->
            val date = startOfMonth.withDayOfMonth(day).format(formatter)

            recordsByDate[date] ?: StepEntity(
                date = date,
                steps = 0,
                baseSensorSteps = 0,
                lastSensorSteps = 0,
                goal = 10000,
                isPaused = false,
                pauseStartSensorSteps = null,
                pausedStepsOffset = 0
            )
        }
    }

    private fun getStartOfCurrentWeek(): String {
        val today = LocalDate.now()
        val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1)
        return startOfWeek.format(formatter)
    }

    private fun getStartOfCurrentMonth(): String {
        return LocalDate.now()
            .withDayOfMonth(1)
            .format(formatter)
    }

    private fun getEndOfCurrentMonth(): String {
        val today = LocalDate.now()

        return today
            .withDayOfMonth(today.lengthOfMonth())
            .format(formatter)
    }
}