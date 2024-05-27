package com.example.myfitfriend.domain.use_case.Workout.exercise

import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke():Flow<Resources<List<Exercise>>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.getExercises()
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