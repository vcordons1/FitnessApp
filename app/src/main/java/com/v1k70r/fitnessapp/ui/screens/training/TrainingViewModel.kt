package com.v1k70r.fitnessapp.ui.screens.training

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.v1k70r.fitnessapp.domain.model.ExerciseSet
import com.v1k70r.fitnessapp.domain.model.LoggedExercise

class TrainingViewModel : ViewModel() {

    private val _loggedExercises = mutableStateListOf<LoggedExercise>()
    val loggedExercises: List<LoggedExercise> = _loggedExercises

    fun addLoggedExercise(
        exerciseName: String,
        sets: List<ExerciseSet>
    ) {
        if (exerciseName.isNotBlank() && sets.isNotEmpty()) {
            _loggedExercises.add(
                LoggedExercise(
                    exerciseName = exerciseName,
                    sets = sets
                )
            )
        }
    }
}