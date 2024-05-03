package com.example.myfitfriend.data.repository

import com.MyFitFriend.data.model.Exercise
import com.MyFitFriend.requests.ExerciseRequest
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.remote.MyFitFriendAPI
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.UserResponse
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import javax.inject.Inject

class MyFitFriendRepositoryIMPL @Inject constructor(val api: MyFitFriendAPI) : MyFitFriendRepository {
    override suspend fun login(userLoginRequest: UserLoginRequest): Int {
        return try {
            api.logIn(userLoginRequest).code()
        } catch (e: Exception) {
            throw e  // Propagate exception to be handled in use case
        }    }

    override suspend fun register(userRegisterRequest: UserRegisterRequest): Int {
        return api.register(userRegisterRequest).code()
    }

    override suspend fun updateUserDetails(userEditRequest: UserEditRequest): Int {

        return api.updateProfile(userEditRequest).code()

    }

    override suspend fun getUserDetails(): User {
        return api.getUserDetails()
    }

    override suspend fun getDietaryLogs(): List<DietaryLogResponse> {
        return api.getDietaryLogs()
    }

    override suspend fun getDietaryLogByDateAndPartOfDay(
        date: String,
        partOfDay: Int
    ): List<DietaryLogResponse> {
        return api.getDietaryLogByDateAndPartOfDay(date,partOfDay)
    }

    override suspend fun insertDietaryLog(dietaryLogRequest: DietaryLogRequest): Int {
return api.insertDietaryLog(dietaryLogRequest).code()
    }

    override suspend fun updateDietaryLog(id: Int, dietaryLogRequest: DietaryLogRequest): Int {
        return api.updateDietaryLog(id,dietaryLogRequest).code()
    }

    override suspend fun deleteDietaryLog(id: Int): Int {
        return api.deleteDietaryLog(id).code()

    }

    override suspend fun getAllFoods(): List<FoodResponse> {
        return api.getAllFoods()
    }

    override suspend fun getFoodById(id: Int): FoodResponse {
        return api.getFoodById(id)
    }

    override suspend fun getFoodByQR(qrCode: String): FoodResponse {
return api.getFoodByQR(qrCode)   }

    override suspend fun getWorkouts(): List<Workout> {
        return api.getWorkouts()
    }

    override suspend fun getWorkoutById(workoutId:Int): Workout {
        return api.getWorkoutById(workoutId)
    }

    override suspend fun getExercisesByWorkoutId(workoutId: Int): List<Exercise> {
        return api.getExercisesByWorkoutId(workoutId)
    }

    override suspend fun addExercise(workoutId: Int, exerciseRequest: ExerciseRequest): Int {
        return api.addExerciseToWorkout(workoutId=workoutId,exerciseRequest=exerciseRequest).code()
    }

    override suspend fun updateExercise(
        workoutId: Int,
        exerciseId: Int,
        exerciseRequest: ExerciseRequest
    ): Int {
return api.updateExerciseToWorkout(workoutId=workoutId,exerciseId=exerciseId, exerciseRequest=exerciseRequest).code()   }

    override suspend fun deleteExercise(exerciseId: Int): Int {
return api.deleteExerciseFromWorkout(exerciseId).code()    }
    ////workouts
    override suspend fun createWorkout(workoutRequest: WorkoutRequest): Int {
        return api.createWorkout(workoutRequest).code()
    }

    override suspend fun deleteWorkoutById(workoutId: Int): Int {
        return api.deleteWorkoutById(workoutId).code()
    }

    override suspend fun editWorkout(workoutId: Int, workoutRequest: WorkoutRequest): Int {
        return api.editWorkout(workoutId,workoutRequest).code()
    }

    override suspend fun getExercise(exerciseId: Int): Exercise {
        return api.getOneExercise(exerciseId)
    }

}