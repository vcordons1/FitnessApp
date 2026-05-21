package com.v1k70r.fitnessapp.ui.screens.training.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.v1k70r.fitnessapp.ui.navigation.TrainingRoutes
import com.v1k70r.fitnessapp.ui.screens.training.TrainingViewModel
import com.v1k70r.fitnessapp.domain.model.ExerciseSet



@Composable
fun TrackExerciseScreen(
    navController: NavHostController,
    category: String,
    exerciseName: String,
    trainingViewModel: TrainingViewModel
) {
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var errorPeso by remember { mutableStateOf(false) }
    var errorRepeticiones by remember { mutableStateOf(false) }
    var indiceSerieEditando by remember { mutableStateOf<Int?>(null) }

    val sets = remember {
        mutableStateListOf<ExerciseSet>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = weight,
                isError = errorPeso,
                supportingText = {
                    if (errorPeso) {
                        Text("Ingresa un peso válido")
                    }
                },
                onValueChange = { weight = it },
                label = {
                    Text("Peso")
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = reps,
                isError = errorRepeticiones,
                supportingText = {
                    if (errorRepeticiones) {
                        Text("Ingresa repeticiones válidas")
                    }
                },
                onValueChange = { reps = it },
                label = {
                    Text("Reps")
                },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                val pesoValido = weight.toDoubleOrNull() != null && weight.toDouble() > 0
                val repeticionesValidas = reps.toIntOrNull() != null && reps.toInt() > 0

                errorPeso = !pesoValido
                errorRepeticiones = !repeticionesValidas

                if (pesoValido && repeticionesValidas) {
                    val nuevaSerie = ExerciseSet(
                        weight = weight,
                        reps = reps
                    )

                    val indiceActual = indiceSerieEditando

                    if (indiceActual == null) {
                        sets.add(nuevaSerie)
                    } else {
                        sets.add(indiceActual, nuevaSerie)
                        indiceSerieEditando = null
                    }

                    weight = ""
                    reps = ""
                    errorPeso = false
                    errorRepeticiones = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (indiceSerieEditando == null) {
                    "Agregar serie"
                } else {
                    "Guardar cambios"
                }
            )
        }

        Button(
            onClick = {
                trainingViewModel.registrarEjercicio(
                    nombreEjercicio = exerciseName,
                    categoria = category,
                    series = sets.toList()
                )

                navController.popBackStack(
                    route = TrainingRoutes.WorkoutLog.route,
                    inclusive = false
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = sets.isNotEmpty()
        ) {
            Text("Guardar ejercicio")
        }

        Text(
            text = "Sets registrados: ${sets.size}",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(sets) { index, set ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Serie ${index + 1}: ${set.weight} kg x ${set.reps} reps"
                        )

                        Button(
                            onClick = {
                                weight = set.weight
                                reps = set.reps
                                indiceSerieEditando = index
                                sets.removeAt(index)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar serie")
                        }

                        Button(
                            onClick = {
                                sets.removeAt(index)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Borrar serie")
                        }
                    }
                }
            }
        }
    }
}