package com.v1k70r.fitnessapp.ui.screens.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v1k70r.fitnessapp.data.repository.TrainingRepository
import com.v1k70r.fitnessapp.domain.model.ExerciseSet
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val repository: TrainingRepository
) : ViewModel() {

    private var sesionActivaId: Long? = null

    val sesionesEntrenamiento = repository
        .obtenerSesionesEntrenamiento()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun registrarEjercicio(
        nombreEjercicio: String,
        categoria: String,
        series: List<ExerciseSet>
    ) {
        if (nombreEjercicio.isBlank() || categoria.isBlank() || series.isEmpty()) return

        val haySerieInvalida = series.any { serie ->
            serie.weight.toDoubleOrNull() == null || serie.reps.toIntOrNull() == null
        }

        if (haySerieInvalida) return

        viewModelScope.launch {
            val idSesion = sesionActivaId ?: repository.crearSesionEntrenamiento().also { nuevaSesionId ->
                sesionActivaId = nuevaSesionId
            }

            repository.registrarEjercicioEnSesion(
                sesionId = idSesion,
                nombreEjercicio = nombreEjercicio,
                categoria = categoria,
                series = series
            )
        }
    }

    fun finalizarSesionActiva() {
        val idSesion = sesionActivaId ?: return

        viewModelScope.launch {
            repository.finalizarSesionEntrenamiento(idSesion)
            sesionActivaId = null
        }
    }

    fun borrarSesionEntrenamiento(sesionId: Long) {
        viewModelScope.launch {
            repository.borrarSesionEntrenamiento(sesionId)

            if (sesionActivaId == sesionId) {
                sesionActivaId = null
            }
        }
    }

    fun actualizarSerieGuardada(
        serieId: Long,
        ejercicioRegistradoId: Long,
        numeroSerie: Int,
        peso: String,
        repeticiones: String
    ) {
        val pesoValido = peso.toDoubleOrNull()
        val repeticionesValidas = repeticiones.toIntOrNull()

        if (pesoValido == null || pesoValido <= 0) return
        if (repeticionesValidas == null || repeticionesValidas <= 0) return

        viewModelScope.launch {
            repository.actualizarSerieGuardada(
                serieId = serieId,
                ejercicioRegistradoId = ejercicioRegistradoId,
                numeroSerie = numeroSerie,
                peso = pesoValido,
                repeticiones = repeticionesValidas
            )
        }
    }

    fun borrarSerieGuardada(serieId: Long) {
        viewModelScope.launch {
            repository.borrarSerieGuardada(serieId)
        }
    }

    fun borrarEjercicioRegistrado(ejercicioRegistradoId: Long) {
        viewModelScope.launch {
            repository.borrarEjercicioRegistrado(ejercicioRegistradoId)
        }
    }
}