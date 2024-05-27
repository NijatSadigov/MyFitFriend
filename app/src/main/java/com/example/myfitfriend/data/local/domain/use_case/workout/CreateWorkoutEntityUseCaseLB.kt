package com.example.myfitfriend.data.local.domain.use_case.workout

import com.example.myfitfriend.data.local.DAO.DietaryLogDAO
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateWorkoutEntityUseCaseLB @Inject constructor(
    private val repository: MyFitFriendLocalRepository
){
operator fun invoke(we:WorkoutEntity):Flow<Resources<Long>> =flow{

try {
        emit(Resources.Loading())

       val response= repository.createWorkout(we)
    //println("GetDietaryLogByIdUseCase : $response")
        emit(Resources.Success(data=response))
}
catch (e:HttpException){
    emit(
        Resources.Error(message =  e.localizedMessage?:"Unknown error occured")
    )
}
    catch (e:IOException){
        emit(
            Resources.Error(message = "could not determine err")
        )
    }

}
}