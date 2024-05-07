package com.example.myfitfriend.domain.use_case.users

import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.UserResponse
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserDetailsByIdUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(id:Int):Flow<Resources<User>> =flow{

        try {
            emit(Resources.Loading<User>())
            val response= repository.getUserDetailsById(id)
            emit(Resources.Success(data=response))
        }
        catch (e:HttpException){
            emit(
                Resources.Error<User>(message =  e.localizedMessage?:"Unknown error occured")
            )
        }
        catch (e:IOException){
            emit(
                Resources.Error<User>(message = "could not determine err")
            )
        }

    }
}