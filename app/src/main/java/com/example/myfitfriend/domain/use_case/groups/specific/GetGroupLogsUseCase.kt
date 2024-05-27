package com.example.myfitfriend.domain.use_case.groups.specific

import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.GroupDietaryLogsItem
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGroupLogsUseCase @Inject constructor(
    private val repository:MyFitFriendRepository
){
    operator fun invoke(groupId:Int):Flow<Resources<List<GroupDietaryLogsItem>>> =flow{

        try {
            emit(Resources.Loading())
            val response= repository.getDietGroupLogs(groupId=groupId,doYouWantGroupDietaryLogItem = true)
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