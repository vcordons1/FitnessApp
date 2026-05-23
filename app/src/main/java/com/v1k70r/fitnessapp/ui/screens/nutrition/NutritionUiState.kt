package com.v1k70r.fitnessapp.ui.screens.nutrition

import com.v1k70r.fitnessapp.domain.model.FoodItem
import com.v1k70r.fitnessapp.domain.model.MealType
import com.v1k70r.fitnessapp.domain.model.NutritionEntry

data class NutritionUiState(
    val entries: List<NutritionEntry> = emptyList(),
    val selectedMealType: MealType = MealType.BREAKFAST,
    val searchQuery: String = "",
    val selectedFood: FoodItem? = null,
    val gramsInput: String = "100"
) {
    val calorieGoal: Int = 3300
    val proteinGoal: Double = 200.0
    val carbsGoal: Double = 400.0
    val fatsGoal: Double = 80.0

    val totalCalories: Int get() = entries.sumOf { it.calories }
    val totalProtein: Double get() = entries.sumOf { it.protein }
    val totalCarbs: Double get() = entries.sumOf { it.carbs }
    val totalFats: Double get() = entries.sumOf { it.fats }

    val remainingCalories: Int get() = calorieGoal - totalCalories
}