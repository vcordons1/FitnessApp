package com.v1k70r.fitnessapp.domain.model

data class ExerciseSet(
    val weight: String,
    val reps: String
)

data class LoggedExercise(
    val exerciseName: String,
    val sets: List<ExerciseSet>
)