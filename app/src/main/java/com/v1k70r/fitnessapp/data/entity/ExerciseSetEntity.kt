package com.v1k70r.fitnessapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_sets",
    foreignKeys = [
        ForeignKey(
            entity = LoggedExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["loggedExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["loggedExerciseId"])
    ]
)
data class ExerciseSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val loggedExerciseId: Long,
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)