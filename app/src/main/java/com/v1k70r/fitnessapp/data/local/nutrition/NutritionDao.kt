package com.v1k70r.fitnessapp.data.local.nutrition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: NutritionEntryEntity)

    @Query("SELECT * FROM foods")
    suspend fun getFoods(): List<FoodEntity>

    @Query("SELECT * FROM nutrition_entries WHERE date = :date")
    fun observeEntriesByDate(date: String): Flow<List<NutritionEntryEntity>>

    @Query("DELETE FROM nutrition_entries WHERE id = :entryId")
    suspend fun deleteEntry(entryId: Long)
}