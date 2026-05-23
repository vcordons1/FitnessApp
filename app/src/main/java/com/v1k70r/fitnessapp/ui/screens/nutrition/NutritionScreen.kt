package com.v1k70r.fitnessapp.ui.screens.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.domain.model.MealType
import com.v1k70r.fitnessapp.ui.screens.nutrition.components.FoodSearchBottomSheet
import com.v1k70r.fitnessapp.ui.screens.nutrition.components.MealCategoryCard
import com.v1k70r.fitnessapp.ui.screens.nutrition.components.NutritionSummaryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    nutritionViewModel: NutritionViewModel = viewModel()
) {
    val state = nutritionViewModel.uiState
    var showFoodSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showFoodSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )  {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar alimento"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp,
                top = 24.dp,
                end = 20.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Nutrición",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Registra alimentos y controla tus macros del día.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                NutritionSummaryCard(state = state)
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Agregar en",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    SingleChoiceSegmentedButtonRow {
                        MealType.entries.forEachIndexed { index, mealType ->
                            SegmentedButton(
                                selected = state.selectedMealType == mealType,
                                onClick = { nutritionViewModel.selectMealType(mealType) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = MealType.entries.size
                                )
                            ) {
                                Text(mealType.label)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Comidas de hoy",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            MealType.entries.forEach { mealType ->
                item {
                    MealCategoryCard(
                        mealType = mealType,
                        entries = nutritionViewModel.entriesByMealType(mealType),
                        onDeleteEntry = nutritionViewModel::deleteEntry
                    )
                }
            }
        }

        if (showFoodSheet) {
            FoodSearchBottomSheet(
                mealType = state.selectedMealType,
                searchQuery = state.searchQuery,
                gramsInput = state.gramsInput,
                selectedFood = state.selectedFood,
                foods = nutritionViewModel.filteredFoods,
                onDismiss = { showFoodSheet = false },
                onSearchChange = nutritionViewModel::onSearchChange,
                onGramsChange = nutritionViewModel::onGramsChange,
                onFoodSelected = nutritionViewModel::selectFood,
                onAddFood = {
                    nutritionViewModel.addSelectedFood()
                    showFoodSheet = false
                }
            )
        }
    }
}