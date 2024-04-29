package com.example.myfitfriend.data.repository

import com.example.myfitfriend.data.remote.MyFitFriendAPI
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.reponses.UserResponse
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

    override suspend fun updateUserDetails(id: Int, userEditRequest: UserEditRequest): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(): UserResponse {
        TODO("Not yet implemented")
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


}