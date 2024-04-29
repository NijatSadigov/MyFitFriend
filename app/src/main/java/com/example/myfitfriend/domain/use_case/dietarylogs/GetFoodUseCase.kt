package com.example.myfitfriend.domain.use_case.dietarylogs

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

class GetFoodUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
operator fun invoke(id:Int):Flow<Resources<FoodResponse>> =flow{

try {
        emit(Resources.Loading<FoodResponse>())
       val response= repository.getFoodById(id)
        emit(Resources.Success(data=response))
}
catch (e:HttpException){
    emit(
        Resources.Error<FoodResponse>(message =  e.localizedMessage?:"Unknown error occured")
    )
}
    catch (e:IOException){
        emit(
            Resources.Error<FoodResponse>(message = "could not determine err")
        )
    }

}
}