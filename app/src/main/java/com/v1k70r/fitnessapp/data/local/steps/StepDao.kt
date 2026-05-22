package com.v1k70r.fitnessapp.data.local.steps

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {

    @Upsert
    suspend fun upsertDailySteps(step: StepEntity)

    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    suspend fun getStepsByDate(date: String): StepEntity?

    @Query("SELECT * FROM daily_steps WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getStepsBetween(
        startDate: String,
        endDate: String
    ): List<StepEntity>

    @Query("SELECT * FROM daily_steps WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun observeStepsBetween(
        startDate: String,
        endDate: String
    ): Flow<List<StepEntity>>
}