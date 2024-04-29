package com.example.myfitfriend.domain.repository

import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.reponses.UserResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest

interface MyFitFriendRepository {

    suspend fun login(userLoginRequest: UserLoginRequest):Int
    suspend fun register(userRegisterRequest: UserRegisterRequest):Int
    suspend fun updateUserDetails( id:Int,userEditRequest: UserEditRequest):Int
    suspend fun getUserDetails():UserResponse
    suspend fun getDietaryLogs():List<DietaryLogResponse>
    suspend fun getDietaryLogByDateAndPartOfDay(date:String, partOfDay:Int):List<DietaryLogResponse>

    suspend fun insertDietaryLog(dietaryLogRequest: DietaryLogRequest):Int
    suspend fun updateDietaryLog(id:Int, dietaryLogRequest: DietaryLogRequest):Int
    suspend fun deleteDietaryLog(id:Int):Int




    suspend fun getAllFoods(): List<FoodResponse>
    suspend fun  getFoodById(id: Int): FoodResponse
    suspend fun getFoodByQR(qrCode:String) : FoodResponse

}