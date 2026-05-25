package com.v1k70r.fitnessapp.ui.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Dashboard : Screen("dashboard", "Inicio")
    data object Training : Screen("training", "Entreno")
    data object Nutrition : Screen("nutrition", "Nutrición")
    data object Pedometer : Screen("pedometer", "Pasos")
    data object Profile : Screen("profile", "Perfil")
}