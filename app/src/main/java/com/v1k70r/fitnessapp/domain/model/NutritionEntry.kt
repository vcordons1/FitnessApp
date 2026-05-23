package com.v1k70r.fitnessapp.domain.model

data class NutritionEntry(
    val id: Long = System.currentTimeMillis(),
    val food: FoodItem,
    val mealType: MealType,
    val grams: Int
) {
    val calories: Int get() = ((food.caloriesPer100g * grams) / 100.0).toInt()
    val protein: Double get() = food.proteinPer100g * grams / 100.0
    val carbs: Double get() = food.carbsPer100g * grams / 100.0
    val fats: Double get() = food.fatsPer100g * grams / 100.0
}