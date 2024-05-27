package com.example.myfitfriend.domain.use_case.Workout

import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateWorkoutUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(workoutRequest: WorkoutEntity):Flow<Resources<Workout>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.createWorkout(workoutRequest)
            //println("response")
            //println(response )
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