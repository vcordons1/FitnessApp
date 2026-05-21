package com.v1k70r.fitnessapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.v1k70r.fitnessapp.data.entity.ExerciseSetEntity
import com.v1k70r.fitnessapp.data.entity.LoggedExerciseEntity
import com.v1k70r.fitnessapp.data.entity.WorkoutSessionEntity
import com.v1k70r.fitnessapp.data.local.dao.LoggedExerciseDao


@Database(
    entities = [
        WorkoutSessionEntity::class,
        LoggedExerciseEntity::class,
        ExerciseSetEntity::class
    ],
    version = 2
)
abstract class FitnessDatabase : RoomDatabase() {

    abstract fun loggedExerciseDao(): LoggedExerciseDao

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