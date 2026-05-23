package com.v1k70r.fitnessapp.data.local.nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_entries")
data class NutritionEntryEntity(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val foodId: Long,
    val mealType: String,
    val grams: Int,
    val date: String
)