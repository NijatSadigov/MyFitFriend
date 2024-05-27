package com.example.myfitfriend.domain.use_case.sync.delete

import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeleteDeletionTableUseCaseLB @Inject constructor(
    private val repository:MyFitFriendLocalRepository
){
operator fun invoke():Flow<Resources<Int>> =flow{

try {
        emit(Resources.Loading<Int>())
       val response= repository.clearDeletionTable()
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