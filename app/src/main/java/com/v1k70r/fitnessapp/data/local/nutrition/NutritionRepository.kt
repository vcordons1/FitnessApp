package com.v1k70r.fitnessapp.data.local.nutrition

import kotlinx.coroutines.flow.Flow

class NutritionRepository(
    private val dao: NutritionDao
) {

    fun observeEntriesByDate(date: String): Flow<List<NutritionEntryEntity>> {
        return dao.observeEntriesByDate(date)
    }

    suspend fun insertEntry(entry: NutritionEntryEntity) {
        dao.insertEntry(entry)
    }

    suspend fun deleteEntry(entryId: Long) {
        dao.deleteEntry(entryId)
    }

    suspend fun getFoods(): List<FoodEntity> {
        return dao.getFoods()
    }

    suspend fun insertFoodsIfNeeded() {
        val existingFoods = dao.getFoods()

        if (existingFoods.isNotEmpty()) return

        dao.insertFoods(
            listOf(
                FoodEntity(1, "Pollo", 165, 31.0, 0.0, 3.6),
                FoodEntity(2, "Arroz", 130, 2.7, 28.0, 0.3),
                FoodEntity(3, "Banano", 89, 1.1, 23.0, 0.3),
                FoodEntity(4, "Huevo", 155, 13.0, 1.1, 11.0),
                FoodEntity(5, "Cereal", 379, 8.0, 84.0, 1.0),
                FoodEntity(6, "Leche", 42, 3.4, 5.0, 1.0),
                FoodEntity(7, "Avena", 389, 16.9, 66.0, 6.9),
                FoodEntity(8, "Manzana", 52, 0.3, 14.0, 0.2),
                FoodEntity(9, "Carne molida", 250, 26.0, 0.0, 15.0),
                FoodEntity(10, "Atún", 132, 28.0, 0.0, 1.0),
                FoodEntity(11, "Papa", 77, 2.0, 17.0, 0.1),
                FoodEntity(12, "Pan", 265, 9.0, 49.0, 3.2),
                FoodEntity(13, "Pasta", 131, 5.0, 25.0, 1.1),
                FoodEntity(14, "Queso", 402, 25.0, 1.3, 33.0),
                FoodEntity(15, "Yogurt", 59, 10.0, 3.6, 0.4),
                FoodEntity(16, "Frijoles", 127, 8.7, 22.8, 0.5),
                FoodEntity(17, "Tortilla", 218, 5.7, 44.6, 2.9),
                FoodEntity(18, "Aguacate", 160, 2.0, 8.5, 14.7),
                FoodEntity(19, "Mantequilla de maní", 588, 25.0, 20.0, 50.0),
                FoodEntity(20, "Salmón", 208, 20.0, 0.0, 13.0)
            )
        )
    }
}