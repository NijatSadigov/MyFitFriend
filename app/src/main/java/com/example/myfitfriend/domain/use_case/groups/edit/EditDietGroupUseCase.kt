package com.example.myfitfriend.domain.use_case.groups.edit

import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditDietGroupUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(groupId: Int,groupName:String):Flow<Resources<Int>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.editDietGroup(groupId,groupName)

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