package com.v1k70r.fitnessapp.ui.screens.nutrition.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.v1k70r.fitnessapp.domain.model.FoodItem
import com.v1k70r.fitnessapp.domain.model.MealType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchBottomSheet(
    mealType: MealType,
    searchQuery: String,
    gramsInput: String,
    selectedFood: FoodItem?,
    foods: List<FoodItem>,
    onDismiss: () -> Unit,
    onSearchChange: (String) -> Unit,
    onGramsChange: (String) -> Unit,
    onFoodSelected: (FoodItem) -> Unit,
    onAddFood: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Agregar alimento",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Destino: ${mealType.label}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = { Text("Buscar alimento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
            )

            OutlinedTextField(
                value = gramsInput,
                onValueChange = onGramsChange,
                label = { Text("Cantidad en gramos") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(foods, key = { it.id }) { food ->
                    FoodRow(
                        food = food,
                        selected = selectedFood?.id == food.id,
                        onClick = { onFoodSelected(food) }
                    )
                }
            }

            Button(
                onClick = onAddFood,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedFood != null && gramsInput.toIntOrNull() != null,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
            ) {
                Text("Agregar alimento")
            }
        }
    }
}