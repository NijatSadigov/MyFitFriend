package com.example.myfitfriend.data.remote

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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MyFitFriendAPI {
    @POST("/login")
    suspend fun logIn(
    @Body userLoginRequest: UserLoginRequest
    ):Response<Unit>

    @POST("/register")
    suspend fun register(
        @Body userRegisterRequest: UserRegisterRequest
    ):Response<Unit>

    @GET("/dietarylog")
    suspend fun getDietaryLogs(
    ):List<DietaryLogResponse>
    @GET("/dietarylog")
    suspend fun getDietaryLogByDateAndPartOfDay(@Query("date") date: String, @Query("partOfDay") partOfDay:Int): List<DietaryLogResponse>

    @GET("/dietarylog")
    suspend fun getDietaryLogById(
        @Query("dietaryLogId") dietaryLogId: Int
    ):DietaryLogResponse

    @POST("/dietarylog")
    suspend fun insertDietaryLog(
        @Body dietaryLogRequest: DietaryLogRequest
    ):Response<Unit>


    @PATCH("dietarylog")
    suspend fun updateDietaryLog(
        @Query("id") id:Int,
        @Body dietaryLogRequest: DietaryLogRequest
    ):Response<Unit>

    @DELETE("dietarylog")
    suspend fun deleteDietaryLog(
        @Query("id") id:Int,
    ):Response<Unit>

    @GET("/foods")
    suspend fun getAllFoods(): List <FoodResponse>

    @GET("/foods")
    suspend fun getFoodById(@Query("id") id: Int): FoodResponse

    @GET("/foods")
    suspend fun getFoodByQR(@Query("qrCode") qrCode: String): FoodResponse

    @GET("/profile")
    suspend fun getUserDetails(): User

    @PATCH("/profile")
    suspend fun updateProfile(
        @Body userEditRequest: UserEditRequest
    ):Response<Unit>
    //Workouts
    @GET("/workout")
    suspend fun getWorkouts(

    ):List<Workout>
    @GET("/workout")
    suspend fun getWorkoutById(
        @Query("workoutId") workoutId: Int
    ):Workout

    @GET ("/exercises")
    suspend fun getExercisesByWorkoutId(
        @Query("workoutId") workoutId: Int

    ):List<Exercise>
    @POST ("/exercises")
    suspend fun addExerciseToWorkout(
        @Query("workoutId") workoutId: Int,
        @Body exerciseRequest: ExerciseRequest

    ):Response<Unit>

    //updateexercise
    @PATCH ("/exercises")
    suspend fun updateExerciseToWorkout(
        @Query("workoutId") workoutId: Int,
        @Query("exerciseId") exerciseId: Int,

        @Body exerciseRequest: ExerciseRequest

    ):Response<Unit>

    @DELETE ("/exercises")
    suspend fun deleteExerciseFromWorkout(
        @Query("exerciseId") exerciseId: Int,

        ):Response<Unit>

    @POST ("/workout")
    suspend fun createWorkout(
        @Body workoutRequest: WorkoutRequest
    ):Response<Unit>
    @DELETE("/workout")
    suspend fun deleteWorkoutById(@Query("workoutId")workoutId:Int):Response<Unit>

    @PATCH("/workout")
    suspend fun editWorkout(
        @Query("workoutId") workoutId: Int,
        @Body workoutRequest:WorkoutRequest
        ):Response<Unit>
    @GET("exercises")
    suspend fun getOneExercise(
        @Query("exerciseId") exerciseId: Int)
:Exercise

    @GET("dietgroup")
    suspend fun getGroups(

    ):List<DietGroup>

    @GET("dietgroup")
    suspend fun getUserDetailsById(
        @Query("groupUserId") groupUserId:Int
    ):User
    @POST("dietgroup")
    suspend fun createGroup(
        @Query("groupname") groupname:String

    ):Response<Unit>
    @GET("dietgroup")
    suspend fun getMembers(
        @Query("groupId") groupId:Int

    ):List<Int>




}