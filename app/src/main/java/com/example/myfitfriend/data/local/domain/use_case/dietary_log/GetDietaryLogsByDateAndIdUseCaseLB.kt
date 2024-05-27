package com.example.myfitfriend.data.local.domain.use_case.dietary_log

import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDietaryLogsByDateAndIdUseCaseLB @Inject constructor(
    private val repository: MyFitFriendLocalRepository
){
operator fun invoke(date:String,partOfDay:Int):Flow<Resources<List<DietaryLogEntity>>> =flow{

try {
        emit(Resources.Loading())

       val response= repository.getDietaryLogByDateAndPartOfDay(todayDate = date,partOfDay=partOfDay)
   // println("GetDietaryLogByIdUseCase : $response")
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