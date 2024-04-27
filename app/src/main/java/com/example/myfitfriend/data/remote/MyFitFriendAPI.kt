package com.example.myfitfriend.data.remote

import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
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
}