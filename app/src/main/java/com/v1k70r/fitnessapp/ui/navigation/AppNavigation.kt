package com.v1k70r.fitnessapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v1k70r.fitnessapp.ui.screens.dashboard.DashboardScreen
import com.v1k70r.fitnessapp.ui.screens.nutrition.NutritionScreen
import com.v1k70r.fitnessapp.ui.screens.pedometer.PedometerScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }

        composable(Screen.Training.route) {
            val trainingNavController = rememberNavController()
            TrainingNavigation(navController = trainingNavController)
        }

        composable(Screen.Nutrition.route) {
            NutritionScreen()
        }

        composable(Screen.Pedometer.route) {
            PedometerScreen()
        }
    }
}