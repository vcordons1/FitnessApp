package com.v1k70r.fitnessapp.domain.model
import com.v1k70r.fitnessapp.MainActivity

data class WorkoutSession(
    val id: Long,
    val startedAt: Long,
    val endedAt: Long?,
    val title: String,
    val exercises: List<LoggedExercise>
)

data class LoggedExercise(
    val id: Long = 0,
    val exerciseName: String,
    val category: String = "",
    val sets: List<ExerciseSet>
)

data class ExerciseSet(
    val id: Long = 0,
    val setNumber: Int = 0,
    val weight: String,
    val reps: String
)