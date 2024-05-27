package com.example.myfitfriend.domain.use_case.groups.create

import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateDietGroupUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(groupName: String):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.createGroup(groupName)
            //println("response")
            //println(response )
            emit(Resources.Success(data=response))
        }
        catch (e:HttpException){
            emit(
                Resources.Error(message =  e.localizedMessage?:"Unknown error occurred")
            )
        }
        catch (e:IOException){
            emit(
                Resources.Error(message = "could not determine err")
            )
        }

    }
}