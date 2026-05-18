package com.v1k70r.fitnessapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.v1k70r.fitnessapp.ui.screens.dashboard.DashboardScreen
import com.v1k70r.fitnessapp.ui.screens.training.TrainingScreen
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
            TrainingScreen()
        }

        composable(Screen.Nutrition.route) {
            NutritionScreen()
        }

        composable(Screen.Pedometer.route) {
            PedometerScreen()
        }
    }
}