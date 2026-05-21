package com.v1k70r.fitnessapp.data.repository

import com.v1k70r.fitnessapp.data.entity.ExerciseSetEntity
import com.v1k70r.fitnessapp.data.entity.WorkoutSessionEntity
import com.v1k70r.fitnessapp.data.local.dao.LoggedExerciseDao
import com.v1k70r.fitnessapp.domain.model.ExerciseSet
import com.v1k70r.fitnessapp.domain.model.LoggedExercise
import com.v1k70r.fitnessapp.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrainingRepository(
    private val loggedExerciseDao: LoggedExerciseDao
) {

    fun obtenerSesionesEntrenamiento(): Flow<List<WorkoutSession>> {
        return loggedExerciseDao.getAllWorkoutSessions().map { sessions ->

            sessions.map { session ->

                val ejercicios = loggedExerciseDao
                    .getExercisesForSession(session.id)
                    .map { exercise ->

                        val series = loggedExerciseDao
                            .getSetsForExercise(exercise.id)
                            .map { set ->

                                ExerciseSet(
                                    id = set.id,
                                    setNumber = set.setNumber,
                                    weight = set.weight.toString(),
                                    reps = set.reps.toString()
                                )
                            }

                        LoggedExercise(
                            id = exercise.id,
                            exerciseName = exercise.exerciseName,
                            category = exercise.category,
                            sets = series
                        )
                    }

                WorkoutSession(
                    id = session.id,
                    startedAt = session.startedAt,
                    endedAt = session.endedAt,
                    title = session.title,
                    exercises = ejercicios
                )
            }
        }
    }

    suspend fun crearSesionEntrenamiento(): Long {
        return loggedExerciseDao.insertWorkoutSession(
            WorkoutSessionEntity(
                startedAt = System.currentTimeMillis(),
                title = "Entrenamiento"
            )
        )
    }

    suspend fun registrarEjercicioEnSesion(
        sesionId: Long,
        nombreEjercicio: String,
        categoria: String,
        series: List<ExerciseSet>
    ) {
        val seriesEntity = series.map { serie ->
            ExerciseSetEntity(
                loggedExerciseId = 0,
                setNumber = 0,
                weight = serie.weight.toDouble(),
                reps = serie.reps.toInt()
            )
        }

        loggedExerciseDao.insertFullExercise(
            sessionId = sesionId,
            exerciseName = nombreEjercicio,
            category = categoria,
            sets = seriesEntity
        )
    }

    suspend fun finalizarSesionEntrenamiento(sesionId: Long) {
        loggedExerciseDao.finishWorkoutSession(
            sessionId = sesionId,
            endedAt = System.currentTimeMillis()
        )
    }

    suspend fun borrarSesionEntrenamiento(sesionId: Long) {
        loggedExerciseDao.deleteFullWorkoutSession(sesionId)
    }

    suspend fun actualizarSerieGuardada(
        serieId: Long,
        ejercicioRegistradoId: Long,
        numeroSerie: Int,
        peso: Double,
        repeticiones: Int
    ) {
        loggedExerciseDao.updateExerciseSet(
            ExerciseSetEntity(
                id = serieId,
                loggedExerciseId = ejercicioRegistradoId,
                setNumber = numeroSerie,
                weight = peso,
                reps = repeticiones
            )
        )
    }

    suspend fun borrarSerieGuardada(serieId: Long) {
        loggedExerciseDao.deleteFullExerciseSet(serieId)
    }

    suspend fun borrarEjercicioRegistrado(ejercicioRegistradoId: Long) {
        loggedExerciseDao.deleteFullLoggedExercise(ejercicioRegistradoId)
    }
}