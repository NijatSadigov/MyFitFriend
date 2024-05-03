package com.example.myfitfriend.domain.use_case.Workout

import com.MyFitFriend.data.model.Exercise
import com.MyFitFriend.requests.ExerciseRequest
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditWorkoutUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
        operator fun invoke(workoutId:Int,workoutRequest: WorkoutRequest):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading<Int>())
            val response= repository.editWorkout(workoutId=workoutId, workoutRequest =workoutRequest )
            println("response")
            println(response )
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