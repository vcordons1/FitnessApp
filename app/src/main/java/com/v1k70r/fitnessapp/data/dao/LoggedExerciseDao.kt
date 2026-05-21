package com.v1k70r.fitnessapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.v1k70r.fitnessapp.data.entity.ExerciseSetEntity
import com.v1k70r.fitnessapp.data.entity.LoggedExerciseEntity
import com.v1k70r.fitnessapp.data.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow
import kotlin.collections.forEachIndexed

@Dao
interface LoggedExerciseDao {

    @Insert
    suspend fun insertWorkoutSession(
        workoutSession: WorkoutSessionEntity
    ): Long

    @Insert
    suspend fun insertLoggedExercise(
        loggedExercise: LoggedExerciseEntity
    ): Long

    @Insert
    suspend fun insertExerciseSet(
        exerciseSet: ExerciseSetEntity
    )

    @Update
    suspend fun updateExerciseSet(
        exerciseSet: ExerciseSetEntity
    )

    @Query("DELETE FROM exercise_sets WHERE id = :serieId")
    suspend fun deleteExerciseSetById(
        serieId: Long
    )

    @Query("SELECT * FROM workout_sessions ORDER BY startedAt DESC")
    fun getAllWorkoutSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM logged_exercises WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    suspend fun getExercisesForSession(
        sessionId: Long
    ): List<LoggedExerciseEntity>

    @Query("SELECT * FROM exercise_sets WHERE loggedExerciseId = :loggedExerciseId ORDER BY setNumber ASC")
    suspend fun getSetsForExercise(
        loggedExerciseId: Long
    ): List<ExerciseSetEntity>

    @Query("SELECT COUNT(*) FROM logged_exercises WHERE sessionId = :sessionId")
    suspend fun getExerciseCountForSession(
        sessionId: Long
    ): Int

    @Query("UPDATE workout_sessions SET endedAt = :endedAt WHERE id = :sessionId")
    suspend fun finishWorkoutSession(
        sessionId: Long,
        endedAt: Long
    )

    @Query("""
    SELECT loggedExerciseId 
    FROM exercise_sets 
    WHERE id = :serieId
""")
    suspend fun getLoggedExerciseIdBySerieId(serieId: Long): Long?

    @Query("""
    SELECT * FROM exercise_sets 
    WHERE loggedExerciseId = :ejercicioRegistradoId
    ORDER BY setNumber ASC
""")
    suspend fun getExerciseSetEntitiesForExercise(
        ejercicioRegistradoId: Long
    ): List<ExerciseSetEntity>

    @Query("""
    SELECT le.sessionId 
    FROM logged_exercises le
    INNER JOIN exercise_sets es ON es.loggedExerciseId = le.id
    WHERE es.id = :serieId
""")
    suspend fun getSessionIdBySerieId(serieId: Long): Long?

    @Query("UPDATE workout_sessions SET title = title WHERE id = :sessionId")
    suspend fun touchWorkoutSession(sessionId: Long)

    @Transaction
    suspend fun deleteFullExerciseSet(serieId: Long) {
        val sessionId = getSessionIdBySerieId(serieId)
        val ejercicioRegistradoId = getLoggedExerciseIdBySerieId(serieId)

        deleteExerciseSetById(serieId)

        if (ejercicioRegistradoId != null) {
            val seriesRestantes = getExerciseSetEntitiesForExercise(ejercicioRegistradoId)

            seriesRestantes.forEachIndexed { index, serie ->
                updateExerciseSet(
                    serie.copy(
                        setNumber = index + 1
                    )
                )
            }
        }

        if (sessionId != null) {
            touchWorkoutSession(sessionId)
        }
    }

    @Query("""
    DELETE FROM exercise_sets 
    WHERE loggedExerciseId IN (
        SELECT id FROM logged_exercises WHERE sessionId = :sessionId
    )
""")
    suspend fun deleteExerciseSetsBySessionId(sessionId: Long)

    @Query("DELETE FROM logged_exercises WHERE sessionId = :sessionId")
    suspend fun deleteLoggedExercisesBySessionId(sessionId: Long)

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    suspend fun deleteWorkoutSessionById(sessionId: Long)

    @Transaction
    suspend fun deleteFullWorkoutSession(sessionId: Long) {
        deleteExerciseSetsBySessionId(sessionId)
        deleteLoggedExercisesBySessionId(sessionId)
        deleteWorkoutSessionById(sessionId)
    }

    @Transaction
    suspend fun insertFullExercise(
        sessionId: Long,
        exerciseName: String,
        category: String,
        sets: List<ExerciseSetEntity>
    ) {
        val orderIndex = getExerciseCountForSession(sessionId)

        val loggedExerciseId = insertLoggedExercise(
            LoggedExerciseEntity(
                sessionId = sessionId,
                exerciseName = exerciseName,
                category = category,
                orderIndex = orderIndex
            )
        )

        sets.forEachIndexed { index, set ->
            insertExerciseSet(
                set.copy(
                    loggedExerciseId = loggedExerciseId,
                    setNumber = index + 1
                )
            )
        }
    }

    @Query("DELETE FROM exercise_sets WHERE loggedExerciseId = :ejercicioRegistradoId")
    suspend fun deleteExerciseSetsByLoggedExerciseId(
        ejercicioRegistradoId: Long
    )

    @Query("DELETE FROM logged_exercises WHERE id = :ejercicioRegistradoId")
    suspend fun deleteLoggedExerciseById(
        ejercicioRegistradoId: Long
    )

    @Query("SELECT sessionId FROM logged_exercises WHERE id = :ejercicioRegistradoId")
    suspend fun getSessionIdByLoggedExerciseId(
        ejercicioRegistradoId: Long
    ): Long?

    @Query("""
    SELECT * FROM logged_exercises
    WHERE sessionId = :sessionId
    ORDER BY orderIndex ASC
""")
    suspend fun getLoggedExercisesForSession(
        sessionId: Long
    ): List<LoggedExerciseEntity>

    @Query("UPDATE logged_exercises SET orderIndex = :orderIndex WHERE id = :ejercicioRegistradoId")
    suspend fun updateLoggedExerciseOrderIndex(
        ejercicioRegistradoId: Long,
        orderIndex: Int
    )

    @Transaction
    suspend fun deleteFullLoggedExercise(
        ejercicioRegistradoId: Long
    ) {
        val sessionId = getSessionIdByLoggedExerciseId(ejercicioRegistradoId)

        deleteExerciseSetsByLoggedExerciseId(ejercicioRegistradoId)
        deleteLoggedExerciseById(ejercicioRegistradoId)

        if (sessionId != null) {
            val ejerciciosRestantes = getLoggedExercisesForSession(sessionId)

            ejerciciosRestantes.forEachIndexed { index, ejercicio ->
                updateLoggedExerciseOrderIndex(
                    ejercicioRegistradoId = ejercicio.id,
                    orderIndex = index
                )
            }

            touchWorkoutSession(sessionId)
        }
    }
}