package com.example.myfitfriend.domain.use_case.groups.delete

import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class KickUserUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(userId:Int,groupId: Int):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.kickUser(userId =userId,groupId=  groupId)

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