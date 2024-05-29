package com.example.myfitfriend.data.repository

import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository

class FakeMyFitFriendLocalRepository : MyFitFriendLocalRepository {
    private val dietaryLogs = mutableListOf<DietaryLogEntity>()
    private val foods = mutableListOf<LocalFoodEntity>()
    private val users = mutableListOf<UserEntity>()
    private val workouts = mutableListOf<WorkoutEntity>()
    private val exercises = mutableListOf<ExerciseEntity>()
    private val deletions = mutableListOf<DeletedItemsEntity>()
    private var user: UserEntity? = null
    override suspend fun getTodayLogsByASCID(todayDate: String): List<DietaryLogEntity> {
        return dietaryLogs.filter { it.date == todayDate }
    }

    override suspend fun clearDietaryLogs(): Int {
        val size = dietaryLogs.size
        dietaryLogs.clear()
        return size
    }

    override suspend fun addLogForToday(dietaryLogEntity: DietaryLogEntity): Long {
        dietaryLogs.add(dietaryLogEntity)
        return dietaryLogEntity.dietaryLogId.toLong()
    }

    override suspend fun editLog(dietaryLogEntity: DietaryLogEntity): Int {
        val index = dietaryLogs.indexOfFirst { it.dietaryLogId == dietaryLogEntity.dietaryLogId }
        return if (index != -1) {
            dietaryLogs[index] = dietaryLogEntity
            1
        } else {
            0
        }
    }

    override suspend fun removeLog(dietaryLogId: Int): Int {
        return if (dietaryLogs.removeIf { it.dietaryLogId == dietaryLogId }) 1 else 0
    }

    override suspend fun getDietaryLogByDateAndPartOfDay(todayDate: String, partOfDay: Int): List<DietaryLogEntity> {
        return dietaryLogs.filter { it.date == todayDate && it.partOfDay == partOfDay }
    }


    fun setUser(userEntity: UserEntity) {
        users.add(userEntity)
        println("setuser $users")
    }

    override suspend fun setDietaryLogs(serverDietaryLogs: List<DietaryLogEntity>): List<Long> {
        dietaryLogs.clear()
        dietaryLogs.addAll(serverDietaryLogs)
        return serverDietaryLogs.map { it.dietaryLogId.toLong() }
    }

    override suspend fun getDietaryLogsLB(): List<DietaryLogEntity> {
        return dietaryLogs
    }

    override suspend fun getDietaryLogByIdLB(dietaryLogId: Int): DietaryLogEntity {
        return dietaryLogs.first { it.dietaryLogId == dietaryLogId }
    }

    override suspend fun getUser(): UserEntity? {
        println("users in fake : $users")
        return users.firstOrNull()
    }

    override suspend fun addAndEditUser(userEntity: UserEntity): Long {
        users.clear()
        users.add(userEntity)
        return userEntity.userId.toLong()
    }

    override suspend fun removeUsers(): Int {
        val size = users.size
        users.clear()
        return size
    }

    override suspend fun getFoodByQRCode(barCode: String): LocalFoodEntity {
        return foods.first { it.qrCode == barCode }
    }

    override suspend fun clearFoods(): Int {
        val size = foods.size
        foods.clear()
        return size
    }

    override suspend fun getAllFoods(): List<LocalFoodEntity> {
        return foods
    }

    override suspend fun getFoodById(foodId: Int): LocalFoodEntity? {
        return foods.firstOrNull { it.foodId == foodId }
    }

    override suspend fun setFoods(foods: List<LocalFoodEntity>): List<Long> {
        this.foods.clear()
        this.foods.addAll(foods)
        return foods.map { it.foodId.toLong() }
    }

    override suspend fun createWorkout(workoutEntity: WorkoutEntity): Long {
        workouts.add(workoutEntity)
        return workouts.size.toLong()
    }

    override suspend fun editWorkout(workoutEntity: WorkoutEntity): Int {
        val index = workouts.indexOfFirst { it.workoutId == workoutEntity.workoutId }
        return if (index != -1) {
            workouts[index] = workoutEntity
            1
        } else {
            0
        }
    }

    override suspend fun deleteWorkout(workoutId: Int): Int {
        return if (workouts.removeIf { it.workoutId == workoutId }) 1 else 0
    }

    override suspend fun getWorkouts(): List<WorkoutEntity> {
        return workouts
    }

    override suspend fun getWorkoutById(workoutId: Int): WorkoutEntity {
        return workouts.first { it.workoutId == workoutId }
    }

    override suspend fun clearAllWorkouts(): Int {
        val size = workouts.size
        workouts.clear()
        return size
    }

    override suspend fun setAllWorkouts(workouts: List<WorkoutEntity>): List<Long> {
        this.workouts.clear()
        this.workouts.addAll(workouts)
        return workouts.map { it.workoutId.toLong() }
    }

    override suspend fun createExercise(exerciseEntity: ExerciseEntity): Long {
        exercises.add(exerciseEntity)
        return exerciseEntity.exerciseId.toLong()
    }

    override suspend fun editExercise(exerciseEntity: ExerciseEntity): Int {
        val index = exercises.indexOfFirst { it.exerciseId == exerciseEntity.exerciseId }
        return if (index != -1) {
            exercises[index] = exerciseEntity
            1
        } else {
            0
        }
    }

    override suspend fun deleteExercise(exerciseId: Int): Int {
        return if (exercises.removeIf { it.exerciseId == exerciseId }) 1 else 0
    }

    override suspend fun getExercises(): List<ExerciseEntity> {
        return exercises
    }

    override suspend fun getExerciseByExerciseId(exerciseId: Int): ExerciseEntity {
        return exercises.first { it.exerciseId == exerciseId }
    }

    override suspend fun getExercisesByWorkoutId(workoutId: Int): List<ExerciseEntity> {
        return exercises.filter { it.workoutId == workoutId }
    }

    override suspend fun clearAllExercises(): Int {
        val size = exercises.size
        exercises.clear()
        return size
    }

    override suspend fun setAllExercises(exercises: List<ExerciseEntity>): List<Long> {
        this.exercises.clear()
        this.exercises.addAll(exercises)
        return exercises.map { it.exerciseId.toLong() }
    }

    override suspend fun getDeletionTable(): List<DeletedItemsEntity> {
        return deletions
    }

    override suspend fun clearDeletionTable(): Int {
        val size = deletions.size
        deletions.clear()
        return size
    }

    override suspend fun insertDeletionEntity(de: DeletedItemsEntity): Long {
        deletions.add(de)
        return de.deletedId.toLong()
    }
}
