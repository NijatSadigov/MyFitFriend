package com.example.myfitfriend.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myfitfriend.data.local.DAO.DietaryLogDAO
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity

@Database(
    entities = [DietaryLogEntity::class , LocalFoodEntity::class, UserEntity::class, WorkoutEntity::class, ExerciseEntity::class,
               DeletedItemsEntity::class
               ],
    version = 8
)
abstract class LocalDB: RoomDatabase() {
    abstract fun dietaryLogDao(): DietaryLogDAO
}