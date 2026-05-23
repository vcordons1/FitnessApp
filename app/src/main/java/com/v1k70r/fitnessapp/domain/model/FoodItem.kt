package com.v1k70r.fitnessapp.domain.model

data class FoodItem(
    val id: Long,
    val name: String,
    val caloriesPer100g: Int,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatsPer100g: Double
)