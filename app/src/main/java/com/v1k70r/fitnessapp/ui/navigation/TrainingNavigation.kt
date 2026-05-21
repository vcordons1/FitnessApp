package com.v1k70r.fitnessapp.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.v1k70r.fitnessapp.ui.screens.training.categories.CategoryScreen
import com.v1k70r.fitnessapp.ui.screens.training.exerciselist.ExerciseListScreen
import com.v1k70r.fitnessapp.ui.screens.training.track.TrackExerciseScreen
import com.v1k70r.fitnessapp.ui.screens.training.workoutlog.WorkoutLogScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.data.repository.TrainingRepository
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel

@Composable
fun TrainingNavigation(
    navController: NavHostController,
    trainingViewModel: TrainingViewModel
) {


    NavHost(
        navController = navController,
        startDestination = TrainingRoutes.WorkoutLog.route
    ) {

        composable(TrainingRoutes.WorkoutLog.route) {
            WorkoutLogScreen(
                navController = navController,
                trainingViewModel = trainingViewModel
            )
        }

        composable(TrainingRoutes.Categories.route) {
            CategoryScreen(navController)
        }

        composable(TrainingRoutes.ExerciseList.route) { backStackEntry ->

            val category = backStackEntry.arguments?.getString("category") ?: ""

            ExerciseListScreen(
                navController = navController,
                category = category
            )
        }

        composable(
            route = TrainingRoutes.TrackExercise.route
        ) { backStackEntry ->

            val category =
                backStackEntry.arguments?.getString("category") ?: ""

            val exerciseName =
                backStackEntry.arguments?.getString("exerciseName") ?: ""

            TrackExerciseScreen(
                navController = navController,
                category = category,
                exerciseName = exerciseName,
                trainingViewModel = trainingViewModel
            )
        }
    }
}


