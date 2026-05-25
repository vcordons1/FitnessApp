package com.v1k70r.fitnessapp.ui.screens.nutrition

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.v1k70r.fitnessapp.data.local.FitnessDatabase
import com.v1k70r.fitnessapp.data.local.nutrition.NutritionEntryEntity
import com.v1k70r.fitnessapp.data.local.nutrition.NutritionRepository
import com.v1k70r.fitnessapp.domain.model.FoodItem
import com.v1k70r.fitnessapp.domain.model.MealType
import com.v1k70r.fitnessapp.domain.model.NutritionEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class NutritionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: NutritionRepository

    private var allFoods: List<FoodItem> = emptyList()

    private val prefs = application.getSharedPreferences("nutrition_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    init {

        val database = FitnessDatabase.getDatabase(application)

        repository = NutritionRepository(
            database.nutritionDao()
        )

        _uiState.value = _uiState.value.copy(
            proteinGoal = prefs.getFloat("protein_goal", 200f).toDouble(),
            carbsGoal = prefs.getFloat("carbs_goal", 400f).toDouble(),
            fatsGoal = prefs.getFloat("fats_goal", 80f).toDouble()
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

                    _uiState.value = _uiState.value.copy(
                        entries = mappedEntries
                    )
                }
        }
    }

    fun filteredFoods(query: String): List<FoodItem> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return allFoods

        return allFoods.filter {
            it.name.contains(trimmed, ignoreCase = true)
        }
    }

    fun selectMealType(mealType: MealType) {
        _uiState.value = _uiState.value.copy(selectedMealType = mealType)
    }

    fun onSearchChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
    }

    fun selectFood(food: FoodItem) {
        _uiState.value = _uiState.value.copy(selectedFood = food)
    }

    fun onGramsChange(value: String) {
        _uiState.value = _uiState.value.copy(
            gramsInput = value.filter { it.isDigit() }
        )
    }

    fun addSelectedFood() {

        val food = _uiState.value.selectedFood ?: return

        val grams = _uiState.value.gramsInput.toIntOrNull()
            ?: return

        viewModelScope.launch {

            repository.insertEntry(
                NutritionEntryEntity(
                    foodId = food.id,
                    mealType = _uiState.value.selectedMealType.name,
                    grams = grams,
                    date = LocalDate.now().toString()
                )
            )

            _uiState.value = _uiState.value.copy(
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
        return _uiState.value.entries.filter {
            it.mealType == mealType
        }
    }

    fun setProteinGoal(goal: Double) {
        val clamped = goal.coerceIn(0.0, 1000.0)
        _uiState.value = _uiState.value.copy(proteinGoal = clamped)
        prefs.edit().putFloat("protein_goal", clamped.toFloat()).apply()
    }

    fun setCarbsGoal(goal: Double) {
        val clamped = goal.coerceIn(0.0, 2000.0)
        _uiState.value = _uiState.value.copy(carbsGoal = clamped)
        prefs.edit().putFloat("carbs_goal", clamped.toFloat()).apply()
    }

    fun setFatsGoal(goal: Double) {
        val clamped = goal.coerceIn(0.0, 500.0)
        _uiState.value = _uiState.value.copy(fatsGoal = clamped)
        prefs.edit().putFloat("fats_goal", clamped.toFloat()).apply()
    }
}