package com.v1k70r.fitnessapp.domain.repository

import androidx.compose.runtime.mutableStateListOf
import com.v1k70r.fitnessapp.data.entity.LoggedExerciseEntity
import com.v1k70r.fitnessapp.domain.model.LoggedExercise

object TrainingRepository {

    private val _loggedExercises =
        mutableStateListOf<LoggedExercise>()

    val loggedExercises: List<LoggedExercise>
        get() = _loggedExercises

    fun addLoggedExercise(
        loggedExercise: LoggedExercise
    ) {
        _loggedExercises.add(loggedExercise)
    }
}