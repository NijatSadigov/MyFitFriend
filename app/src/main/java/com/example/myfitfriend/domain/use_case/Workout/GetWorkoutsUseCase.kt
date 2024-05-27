package com.example.myfitfriend.domain.use_case.Workout

import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWorkoutsUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
operator fun invoke():Flow<Resources<List<Workout>>> =flow{

try {
        emit(Resources.Loading<List<Workout>>())
       val response= repository.getWorkouts()
    //println("response")
    //println(response )
        emit(Resources.Success(data=response))
}
catch (e:HttpException){
    emit(
        Resources.Error<List<Workout>>(message =  e.localizedMessage?:"Unknown error occured")
    )
}
    catch (e:IOException){
        emit(
            Resources.Error<List<Workout>>(message = "could not determine err")
        )
    }

}
}