package com.example.myfitfriend.data.repository

import androidx.room.Query
import androidx.room.Transaction
import com.example.myfitfriend.data.local.DAO.DietaryLogDAO
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.remote.MyFitFriendAPI
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import javax.inject.Inject

class MyFitFriendLocalRepositoryIMPL@Inject constructor(
    private val dao: DietaryLogDAO) :
    MyFitFriendLocalRepository {

    override suspend fun getTodayLogsByASCID(todayDate: String): List<DietaryLogEntity> {
        return dao.getTodayLogsByASCID(todayDate)
    }

    override suspend fun clearDietaryLogs(): Int {
        return dao.clearDietaryLogs()
    }

    override suspend fun setDietaryLogs(serverDietaryLogs: List<DietaryLogEntity>): List<Long> {
        return dao.insertAllDietaryLogs(serverDietaryLogs)
    }

    override suspend fun addLogForToday(dietaryLogEntity: DietaryLogEntity): Long {
        return dao.addLogForToday(dietaryLogEntity)
    }

    override suspend fun editLog(dietaryLogEntity: DietaryLogEntity): Int {
        return dao.editLog(dietaryLogEntity)
    }

    override suspend fun removeLog(dietaryLogId: Int): Int {
        return dao.removeLog(dietaryLogId)
    }

    override suspend fun getDietaryLogByDateAndPartOfDay(todayDate: String, partOfDay: Int): List<DietaryLogEntity> {
        return dao.getDietaryLogByDateAndPartOfDay(todayDate, partOfDay)
    }

    override suspend fun getDietaryLogsLB():List<DietaryLogEntity>  {
        return dao.getDietaryLogs()
    }

    override suspend fun getDietaryLogByIdLB(dietaryLogId: Int): DietaryLogEntity {
        return dao.getDietaryLogById(dietaryLogId)
    }

    override suspend fun getFoodByQRCode(barCode: String): LocalFoodEntity {
        return dao.getFoodByQRCode(barCode)
    }

    override suspend fun getUser(): UserEntity? {
return dao.getUser()
    }

    override suspend fun addAndEditUser(userEntity: UserEntity): Long {
return dao.addAndUpdateUser(userEntity)   }

    override suspend fun removeUsers(): Int {
return dao.clearAllUsers()   }

    override suspend fun clearFoods(): Int {
        return dao.clearFoods()
    }

    override suspend fun getAllFoods(): List<LocalFoodEntity> {
    return dao.getAllFoods()
    }

    override suspend fun getFoodById(foodId: Int): LocalFoodEntity? {
        return dao.getFoodById(foodId)
        }

    override suspend fun setFoods(foods: List<LocalFoodEntity>): List<Long> {
        return dao.insertAllFoods(foods)
    }

    //workouts
    override suspend fun createWorkout(workoutEntity: WorkoutEntity): Long {
        return dao.createWorkout(workoutEntity)
    }

    override suspend fun editWorkout(workoutEntity: WorkoutEntity): Int {
return dao.editWorkout(workoutEntity)
    }

    @Transaction
    override suspend fun deleteWorkout(workoutId: Int): Int {
        return dao.deleteWorkoutAndExercises(workoutId)
    }

    override suspend fun getWorkouts(): List<WorkoutEntity> {
        return dao.getWorkouts()
    }

    override suspend fun getWorkoutById(workoutId: Int): WorkoutEntity {
        return dao.getWorkoutById(workoutId)
    }

    override suspend fun clearAllWorkouts(): Int {
        return dao.clearAllWorkouts()
    }

    override suspend fun setAllWorkouts(workouts: List<WorkoutEntity>): List<Long> {
       return dao.setAllWorkouts(workouts)
    }

    //exercises
    override suspend fun createExercise(exerciseEntity: ExerciseEntity): Long {
        return dao.createExercise(exerciseEntity)
    }

    override suspend fun editExercise(exerciseEntity: ExerciseEntity): Int {
        return dao.editExercise(exerciseEntity)
    }

    override suspend fun deleteExercise(exerciseId: Int): Int {
       return dao.deleteExercise(exerciseId)
    }

    override suspend fun getExercises(): List<ExerciseEntity> {
       return dao.getExercises()
    }

    override suspend fun getExerciseByExerciseId(exerciseId: Int): ExerciseEntity {
        return dao.getExerciseByExerciseId(exerciseId)
    }

    override suspend fun getExercisesByWorkoutId(workoutId: Int): List<ExerciseEntity> {
return dao.getExercisesByWorkoutId(workoutId)   }

    override suspend fun clearAllExercises(): Int {
        return dao.clearAllExercises()
    }

    override suspend fun setAllExercises(exercises: List<ExerciseEntity>): List<Long> {
        return dao.setAllExercises(exercises)
    }

    override suspend fun getDeletionTable():List<DeletedItemsEntity>{
        return dao.getDeletionTable()
    }

    override suspend fun clearDeletionTable():Int{
return dao.clearDeletionTable()
    }

    override suspend fun insertDeletionEntity(de:DeletedItemsEntity): Long {
        return dao.insertDeletionEntity(de)
    }
}