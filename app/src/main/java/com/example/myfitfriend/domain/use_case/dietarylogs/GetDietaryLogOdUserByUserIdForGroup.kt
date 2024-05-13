package com.example.myfitfriend.domain.use_case.dietarylogs

import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDietaryLogOdUserByUserIdForGroup @Inject constructor(
    private val repository:MyFitFriendRepository
){
operator fun invoke(wantedUserId:Int):Flow<Resources<List<DietaryLogResponse>>> =flow{

try {
        emit(Resources.Loading<List<DietaryLogResponse>>())
       val response= repository.getDietaryLogOfUserByUserIdForGroup(wantedUserId)
    println("GetDietaryLogByIdUseCase : $response")
        emit(Resources.Success(data=response))
}
catch (e:HttpException){
    emit(
        Resources.Error<List<DietaryLogResponse>>(message =  e.localizedMessage?:"Unknown error occured")
    )
}
    catch (e:IOException){
        emit(
            Resources.Error<List<DietaryLogResponse>>(message = "could not determine err")
        )
    }

}
}