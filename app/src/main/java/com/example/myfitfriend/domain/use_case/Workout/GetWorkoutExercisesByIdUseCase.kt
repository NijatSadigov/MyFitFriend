package com.example.myfitfriend.domain.use_case.Workout

import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWorkoutExercisesByIdUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(workoutId:Int):Flow<Resources<List<Exercise>>> =flow{

        try {
            emit(Resources.Loading<List<Exercise>>())
            val response= repository.getExercisesByWorkoutId(workoutId)
            //println("response")
            //println(response )
            emit(Resources.Success(data=response))
        }
        catch (e:HttpException){
            emit(
                Resources.Error<List<Exercise>>(message =  e.localizedMessage?:"Unknown error occured")
            )
        }
        catch (e:IOException){
            emit(
                Resources.Error<List<Exercise>>(message = "could not determine err")
            )
        }

    }
}