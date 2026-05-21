package com.v1k70r.fitnessapp.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.data.repository.TrainingRepository
import com.v1k70r.fitnessapp.ui.screens.dashboard.DashboardScreen
import com.v1k70r.fitnessapp.ui.screens.nutrition.NutritionScreen
import com.v1k70r.fitnessapp.ui.screens.pedometer.PedometerScreen
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext as Application

    val database = FitnessDatabase.getDatabase(context)

    val repository = TrainingRepository(
        loggedExerciseDao = database.loggedExerciseDao()
    )

    val trainingViewModel: TrainingViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TrainingViewModel(repository) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                trainingViewModel = trainingViewModel
            )
        }

        composable(Screen.Training.route) {
            val trainingNavController = rememberNavController()

            TrainingNavigation(
                navController = trainingNavController,
                trainingViewModel = trainingViewModel
            )
        }

        composable(Screen.Nutrition.route) {
            NutritionScreen()
        }

        composable(Screen.Pedometer.route) {
            PedometerScreen()
        }
    }
}