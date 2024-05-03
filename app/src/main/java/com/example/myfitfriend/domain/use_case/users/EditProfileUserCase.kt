package com.example.myfitfriend.domain.use_case.users
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.UserResponse
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditProfileUserCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(
       userEditRequest: UserEditRequest
    ):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading<Int>())
            val response= repository.updateUserDetails(userEditRequest)
            emit(Resources.Success(data=response))
        }
        catch (e:HttpException){
            emit(
                Resources.Error<Int>(message =  e.localizedMessage?:"Unknown error occurred")
            )
        }
        catch (e:IOException){
            emit(
                Resources.Error<Int>(message = "could not determine err")
            )
        }

    }
}