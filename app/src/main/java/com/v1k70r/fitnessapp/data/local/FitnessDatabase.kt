package com.v1k70r.fitnessapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.v1k70r.fitnessapp.data.entity.ExerciseSetEntity
import com.v1k70r.fitnessapp.data.entity.LoggedExerciseEntity
import com.v1k70r.fitnessapp.data.entity.WorkoutSessionEntity
import com.v1k70r.fitnessapp.data.local.dao.LoggedExerciseDao
import com.v1k70r.fitnessapp.data.local.steps.StepDao
import com.v1k70r.fitnessapp.data.local.steps.StepEntity


@Database(
    entities = [
        WorkoutSessionEntity::class,
        LoggedExerciseEntity::class,
        ExerciseSetEntity::class,
        StepEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {

    abstract fun loggedExerciseDao(): LoggedExerciseDao

    abstract fun stepDao(): StepDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}