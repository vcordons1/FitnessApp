package com.v1k70r.fitnessapp.ui.screens.nutrition

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.data.local.nutrition.NutritionEntryEntity
import com.v1k70r.fitnessapp.data.local.nutrition.NutritionRepository
import com.v1k70r.fitnessapp.domain.model.FoodItem
import com.v1k70r.fitnessapp.domain.model.MealType
import com.v1k70r.fitnessapp.domain.model.NutritionEntry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class NutritionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: NutritionRepository

    private var allFoods: List<FoodItem> = emptyList()

    var uiState by mutableStateOf(NutritionUiState())
        private set

    init {

        val database = FitnessDatabase.getDatabase(application)

        repository = NutritionRepository(
            database.nutritionDao()
        )

        initializeNutrition()
    }

    private fun initializeNutrition() {

        viewModelScope.launch {

            repository.insertFoodsIfNeeded()

            allFoods = repository.getFoods().map {
                FoodItem(
                    id = it.id,
                    name = it.name,
                    caloriesPer100g = it.caloriesPer100g,
                    proteinPer100g = it.proteinPer100g,
                    carbsPer100g = it.carbsPer100g,
                    fatsPer100g = it.fatsPer100g
                )
            }

            observeTodayEntries()
        }
    }


    private fun observeTodayEntries() {

        val today = LocalDate.now().toString()

        viewModelScope.launch {

            repository.observeEntriesByDate(today)
                .collectLatest { entities ->

                    val mappedEntries = entities.mapNotNull { entity ->

                        val food = allFoods.find { it.id == entity.foodId }
                            ?: return@mapNotNull null

                        NutritionEntry(
                            id = entity.id,
                            food = food,
                            mealType = MealType.valueOf(entity.mealType),
                            grams = entity.grams
                        )
                    }

                    uiState = uiState.copy(
                        entries = mappedEntries
                    )
                }
        }
    }

    val filteredFoods: List<FoodItem>
        get() {

            val query = uiState.searchQuery.trim()

            return if (query.isBlank()) {
                allFoods
            } else {
                allFoods.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
        }

    fun selectMealType(mealType: MealType) {
        uiState = uiState.copy(selectedMealType = mealType)
    }

    fun onSearchChange(value: String) {
        uiState = uiState.copy(searchQuery = value)
    }

    fun selectFood(food: FoodItem) {
        uiState = uiState.copy(selectedFood = food)
    }

    fun onGramsChange(value: String) {
        uiState = uiState.copy(
            gramsInput = value.filter { it.isDigit() }
        )
    }

    fun addSelectedFood() {

        val food = uiState.selectedFood ?: return

        val grams = uiState.gramsInput.toIntOrNull()
            ?: return

        viewModelScope.launch {

            repository.insertEntry(
                NutritionEntryEntity(
                    foodId = food.id,
                    mealType = uiState.selectedMealType.name,
                    grams = grams,
                    date = LocalDate.now().toString()
                )
            )

            uiState = uiState.copy(
                selectedFood = null,
                searchQuery = "",
                gramsInput = "100"
            )
        }
    }

    fun deleteEntry(entryId: Long) {

        viewModelScope.launch {
            repository.deleteEntry(entryId)
        }
    }

    fun entriesByMealType(mealType: MealType): List<NutritionEntry> {
        return uiState.entries.filter {
            it.mealType == mealType
        }
    }
}