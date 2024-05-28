package com.example.myfitfriend.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.remote.reponses.FoodResponse

interface MyFitFriendLocalRepository {


    suspend fun getTodayLogsByASCID(todayDate: String): List<DietaryLogEntity>
    suspend fun clearDietaryLogs():Int
    suspend fun addLogForToday(dietaryLogEntity: DietaryLogEntity):Long
    suspend fun editLog(dietaryLogEntity: DietaryLogEntity):Int
    suspend fun removeLog(dietaryLogId: Int):Int
    suspend fun getDietaryLogByDateAndPartOfDay(todayDate: String , partOfDay:Int):List<DietaryLogEntity>
    suspend fun setDietaryLogs(serverDietaryLogs: List<DietaryLogEntity>):List<Long>
    suspend fun getDietaryLogsLB(): List<DietaryLogEntity>
    suspend fun getDietaryLogByIdLB(dietaryLogId:Int):DietaryLogEntity
    //user funcs
    suspend fun getUser():UserEntity?
    suspend fun addAndEditUser(userEntity: UserEntity):Long
    suspend fun removeUsers():Int
    suspend fun getFoodByQRCode(barCode:String):LocalFoodEntity
    //foods
    suspend fun clearFoods():Int

    suspend fun getAllFoods():List<LocalFoodEntity>

    suspend fun getFoodById(foodId:Int): LocalFoodEntity?

    suspend fun setFoods(foods :List<LocalFoodEntity>):List<Long>


    //WORKOUTS
    suspend fun createWorkout(workoutEntity: WorkoutEntity):Long


    suspend fun editWorkout(workoutEntity: WorkoutEntity):Int

    suspend fun deleteWorkout(workoutId: Int):Int

    suspend fun getWorkouts():List<WorkoutEntity>

    suspend fun getWorkoutById(workoutId:Int): WorkoutEntity

    suspend fun clearAllWorkouts(): Int

    suspend fun setAllWorkouts(workouts: List<WorkoutEntity>):List<Long>

    //EXERCISES

    suspend fun createExercise(exerciseEntity: ExerciseEntity):Long

    suspend fun editExercise(exerciseEntity: ExerciseEntity):Int


    suspend fun deleteExercise(exerciseId: Int):Int

    suspend fun getExercises():List<ExerciseEntity>

    suspend fun getExerciseByExerciseId(exerciseId:Int): ExerciseEntity

    suspend fun getExercisesByWorkoutId(workoutId:Int):List<ExerciseEntity>

    suspend fun clearAllExercises(): Int

    suspend fun setAllExercises(exercises: List<ExerciseEntity>):List<Long>
    suspend fun getDeletionTable():List<DeletedItemsEntity>

    suspend fun clearDeletionTable():Int

    suspend fun insertDeletionEntity(de:DeletedItemsEntity):Long



}