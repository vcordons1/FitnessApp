package com.v1k70r.fitnessapp.ui.navigation

sealed class TrainingRoutes(
    val route: String
) {
    data object WorkoutLog : TrainingRoutes("workout_log")

    data object Categories : TrainingRoutes("categories")

    data object ExerciseList : TrainingRoutes("exercise_list/{category}") {
        fun createRoute(category: String): String {
            return "exercise_list/$category"
        }
    }

    data object TrackExercise : TrainingRoutes("track_exercise/{exerciseName}") {
        fun createRoute(exerciseName: String): String {
            return "track_exercise/$exerciseName"
        }
    }
}