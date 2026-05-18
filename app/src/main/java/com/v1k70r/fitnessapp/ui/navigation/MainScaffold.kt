package com.v1k70r.fitnessapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Dashboard,
        Screen.Training,
        Screen.Nutrition,
        Screen.Pedometer
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(screen.route)
                        },
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Dashboard -> Icons.Default.Home
                                    Screen.Training -> Icons.Default.FitnessCenter
                                    Screen.Nutrition -> Icons.Default.LocalDining
                                    Screen.Pedometer -> Icons.Default.DirectionsWalk
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(text = screen.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}