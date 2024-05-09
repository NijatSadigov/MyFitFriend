package com.example.myfitfriend.domain.repository

import com.MyFitFriend.data.model.Exercise
import com.MyFitFriend.requests.ExerciseRequest
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.UserResponse
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest
import retrofit2.Response

interface MyFitFriendRepository {

    suspend fun login(userLoginRequest: UserLoginRequest):Int
    suspend fun register(userRegisterRequest: UserRegisterRequest):Int
    suspend fun updateUserDetails( userEditRequest: UserEditRequest):Int
    suspend fun getUserDetails():User
    suspend fun getDietaryLogs():List<DietaryLogResponse>
    suspend fun getDietaryLogByDateAndPartOfDay(date:String, partOfDay:Int):List<DietaryLogResponse>
    suspend fun getDietaryLogById(dietaryLogId:Int):DietaryLogResponse
    suspend fun insertDietaryLog(dietaryLogRequest: DietaryLogRequest):Int
    suspend fun updateDietaryLog(id:Int, dietaryLogRequest: DietaryLogRequest):Int
    suspend fun deleteDietaryLog(id:Int):Int




    suspend fun getAllFoods(): List<FoodResponse>
    suspend fun  getFoodById(id: Int): FoodResponse
    suspend fun getFoodByQR(qrCode:String) : FoodResponse


    //workouts
    suspend fun getWorkouts():List<Workout>
    suspend fun getWorkoutById(workoutId:Int):Workout
    suspend fun getExercisesByWorkoutId(workoutId: Int):List<Exercise>

    suspend fun createWorkout(workoutRequest: WorkoutRequest):Int
    suspend fun deleteWorkoutById(workoutId: Int):Int
    suspend fun editWorkout(workoutId: Int,workoutRequest: WorkoutRequest):Int

    suspend fun getExercise(exerciseId:Int):Exercise
    suspend fun addExercise(workoutId: Int, exerciseRequest: ExerciseRequest):Int
    suspend fun updateExercise(workoutId: Int,exerciseId:Int, exerciseRequest: ExerciseRequest):Int
    suspend fun deleteExercise(exerciseId:Int):Int

//groups
    suspend fun getGroupsOfUser():List<DietGroup>
    suspend fun getUserDetailsById(userId:Int):User
    suspend fun createGroup(groupName:String):Int
    suspend fun getGroupMembersByGroupId(groupId:Int):List<Int>

}