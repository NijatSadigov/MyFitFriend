package com.example.myfitfriend.presentation.dietarylogsperpartofday

import androidx.annotation.Nullable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.asServerResponse
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.DeleteDietaryLogByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogsByDateAndIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
import com.example.myfitfriend.data.remote.reponses.DietaryLogFrameItem
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.DeleteDietaryLogUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogByDateAndPartOfDayUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetFoodUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.InsertDietaryLogCase
import com.example.myfitfriend.domain.use_case.dietarylogs.ShowFoodsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.UpdateDietaryLogUseCase
import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
import com.example.myfitfriend.util.Resources
import com.example.myfitfriend.util.SyncOperationsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SpecificPartOfDayDietaryLogsScreenViewModel @Inject constructor(
    private val getFoodUseCase: GetFoodByIdUseCaseLB,
    private val getDietaryLogByDateAndPartOfDayUseCase: GetDietaryLogsByDateAndIdUseCaseLB,
    private val deleteDietaryByIdLogUseCaseLB: DeleteDietaryLogByIdUseCaseLB,
    private val insertDeletionTableUseCaseLB: InsertDeletionTableUseCaseLB,

):ViewModel() {

    private val _dietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    private val dietaryLogs: State<List<DietaryLogEntity>> =_dietaryLogs


    private val _totalCalories = mutableStateOf(0.0)
    val totalCalories: State<Double> = _totalCalories

    private val _totalCarbs = mutableStateOf(0.0)
    val totalCarbs: State<Double> = _totalCarbs

    private val _totalFats = mutableStateOf(0.0)
    val totalFats: State<Double> = _totalFats

    private val _totalProtein = mutableStateOf(0.0)
    val totalProtein: State<Double> = _totalProtein

    private val _date = mutableStateOf(LocalDate.now().toString())
    val date : State<String> =_date

    private val _dietaryLogsFrame = mutableStateOf<List<DietaryLogFrameItem>>(emptyList())
    val dietaryLogsFrame: State <List<DietaryLogFrameItem>> = _dietaryLogsFrame









    fun onDeleteDietaryLog(partOfDay: Int,dietaryLogId:Int,isAdded:Boolean){
viewModelScope.launch {

    deleteDietaryByIdLogUseCaseLB.invoke(dietaryLogId).onEach {
            result->
        when(result){
            is Resources.Error -> {}
            is Resources.Loading -> {}
            is Resources.Success -> {
                if (result.data==1){
                    if(isAdded)
                    insertDeletionEntity(dietaryLogId)

                    getDietaryLogs(partOfDay)
                }
            }
        }

    }.launchIn(viewModelScope)
}
    }
    private fun insertDeletionEntity(id:Int){
        viewModelScope.launch {
            insertDeletionTableUseCaseLB.invoke(DeletedItemsEntity(
                typeOfItem =0,
                deletedId =id
            )
            ).onEach {
                r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {

                    }
                }
            } .launchIn(viewModelScope)
        }

    }




     fun getDietaryLogs( partOfDay: Int ){
        viewModelScope.launch {
            getDietaryLogByDateAndPartOfDayUseCase(date.value,partOfDay).onEach{
                    result->
                when(result){
                    is Resources.Error -> {
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data!=null) {
                            _dietaryLogs.value = result.data

                            calculateTotalMacrosAndGetLogsFrame()
                        }
                        println(dietaryLogsFrame.value)
                    }
                }

            }.launchIn(viewModelScope)
        }

    }

    private fun calculateTotalMacrosAndGetLogsFrame() {
        _totalCalories.value = 0.0  // Reset before calculation
        _totalCarbs.value = 0.0
        _totalProtein.value = 0.0
        _totalFats.value = 0.0
        val mutableList = mutableListOf<DietaryLogFrameItem>()

        viewModelScope.launch {
            // Use async to fetch all food details concurrently
            val deferredResponses = dietaryLogs.value.map { log ->
                async {
                    getFoodUseCase(log.foodId).collect { result ->
                        when (result) {
                            is Resources.Error -> Unit
                            is Resources.Loading -> Unit
                            is Resources.Success -> {
                                if (result.data != null) {
                                    val currentCalories = result.data.cal
                                    val currentCarbs = result.data.carb
                                    val currentProtein = result.data.protein
                                    val currentFat = result.data.fat

                                    _totalCalories.value += currentCalories * log.amountOfFood / 100.0
                                    _totalCarbs.value += currentCarbs * log.amountOfFood / 100
                                    _totalProtein.value += currentProtein * log.amountOfFood / 100
                                    _totalFats.value += currentFat * log.amountOfFood / 100

                                    mutableList.add(
                                        DietaryLogFrameItem(
                                            foodName = result.data.foodName,
                                            cal = result.data.cal * log.amountOfFood/100,
                                            protein = result.data.protein * log.amountOfFood/100,
                                            carb = result.data.carb * log.amountOfFood/100,
                                            foodId = result.data.foodId.toString(),
                                            fat = result.data.fat * log.amountOfFood/100,
                                            qrCode = result.data.qrCode,
                                            dietaryLogId = log.dietaryLogId,
                                            isSync =log.isSync,
                                            isAdded = log.isAdded
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Wait for all async operations to complete
            deferredResponses.awaitAll()

            // Now mutableList is fully populated and can be used
            //println("mutableList: $mutableList")
            _dietaryLogsFrame.value = mutableList
            println("getlogs ${dietaryLogsFrame.value}")

        }
    }


}
