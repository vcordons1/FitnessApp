package com.v1k70r.fitnessapp.data.local.nutrition

import com.v1k70r.fitnessapp.domain.model.FoodItem

object FoodDatabase {

    val foods = listOf(
        FoodItem(1, "Pollo", 165, 31.0, 0.0, 3.6),
        FoodItem(2, "Arroz", 130, 2.7, 28.0, 0.3),
        FoodItem(3, "Banano", 89, 1.1, 23.0, 0.3),
        FoodItem(4, "Huevo", 155, 13.0, 1.1, 11.0),
        FoodItem(5, "Cereal", 379, 8.0, 84.0, 1.0),
        FoodItem(6, "Leche", 42, 3.4, 5.0, 1.0),
        FoodItem(7, "Avena", 389, 16.9, 66.0, 6.9),
        FoodItem(8, "Manzana", 52, 0.3, 14.0, 0.2),
        FoodItem(9, "Carne molida", 250, 26.0, 0.0, 15.0),
        FoodItem(10, "Atún", 132, 28.0, 0.0, 1.0),
        FoodItem(11, "Papa", 77, 2.0, 17.0, 0.1),
        FoodItem(12, "Pan", 265, 9.0, 49.0, 3.2),
        FoodItem(13, "Pasta", 131, 5.0, 25.0, 1.1),
        FoodItem(14, "Queso", 402, 25.0, 1.3, 33.0),
        FoodItem(15, "Yogurt", 59, 10.0, 3.6, 0.4),
        FoodItem(16, "Frijoles", 127, 8.7, 22.8, 0.5),
        FoodItem(17, "Tortilla", 218, 5.7, 44.6, 2.9),
        FoodItem(18, "Aguacate", 160, 2.0, 8.5, 14.7),
        FoodItem(19, "Mantequilla de maní", 588, 25.0, 20.0, 50.0),
        FoodItem(20, "Salmón", 208, 20.0, 0.0, 13.0)
    )
}