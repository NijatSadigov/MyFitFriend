package com.example.myfitfriend.domain.use_case.Workout.exercise

import com.MyFitFriend.requests.ExerciseRequest
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddExerciseToWorkoutUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(workoutId:Int,exerciseRequest: ExerciseEntity):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading<Int>())
            val response= repository.addExercise(workoutId=workoutId,exerciseRequest)
            //println("response")
            //println(response )
            emit(Resources.Success(data=response))
        }
        catch (e:HttpException){
            emit(
                Resources.Error<Int>(message =  e.localizedMessage?:"Unknown error occured")
            )
        }
        catch (e:IOException){
            emit(
                Resources.Error<Int>(message = "could not determine err")
            )
        }

    }
}