package com.example.myfitfriend.presentation.dietarylogs.editdietarylog

import androidx.annotation.Nullable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.UpdateDietaryLogsEntityUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.requests.DietaryLogRequest
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogByIdUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetFoodUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.InsertDietaryLogCase
import com.example.myfitfriend.domain.use_case.dietarylogs.ShowFoodsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.UpdateDietaryLogUseCase
import com.example.myfitfriend.util.Resources
import com.example.myfitfriend.util.SyncOperationsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDietaryLogScreenViewModel @Inject constructor(
    private val getFoodUseCase: GetFoodByIdUseCaseLB,
    private val updateDietaryLogUseCaseLB: UpdateDietaryLogsEntityUseCaseLB,
    private val getDietaryLogByIdUseCase: GetDietaryLogByIdUseCaseLB

):ViewModel() {
    private val _amountOfFood = mutableStateOf("100") //SHOULD be DOUBLE
    val amountOfFood : State<String> =_amountOfFood
    /*
    private val _foodId = mutableStateOf(0)
    val foodId:State<Int> =_foodId
    */

    private val _partOfDay = mutableStateOf(0)
    val partOfDay: State<Int> =_partOfDay

    private val _foodName = mutableStateOf("")
    val foodName:State<String> =_foodName

    private val _date = mutableStateOf("")
    val date:State<String> =_date


    private val _foodCal = mutableStateOf(0.0)
    val foodCal:State<Double> =_foodCal
    private val _foodProtein = mutableStateOf(0.0)
    val foodProtein:State<Double> =_foodProtein
    private val _foodCarb = mutableStateOf(0.0)
    val foodCarb:State<Double> =_foodCarb
    private val _foodFat = mutableStateOf(0.0)
    val foodFat:State<Double> =_foodFat

    private val _foodIdOfLog = mutableStateOf(0)
    val foodIdOfLog:State<Int> =_foodIdOfLog

    private val _dietaryLogId = mutableStateOf(0)
    val dietaryLogId: State<Int> = _dietaryLogId



    private val _successfulSubmission= mutableStateOf(false)
     val successfulSubmission :State<Boolean> = _successfulSubmission


    fun onPartOfDayChange(partOfDay:Int){
        _partOfDay.value=partOfDay
    }
    fun onAmountOfFoodChange(amountOfFoodI:String){
        if(amountOfFoodI=="") {
            _amountOfFood.value = "0"
        }
        else
        _amountOfFood.value=amountOfFoodI

        viewModelScope.launch {
            getFoodUseCase(foodIdOfLog.value).onEach {

                    result->
                when(result){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null) {
                            println(result.data)
                            _foodCal.value=result.data.cal * amountOfFood.value.toDouble() /100
                            _foodProtein.value=result.data.protein  * amountOfFood.value.toDouble() /100
                            _foodCarb.value=result.data.carb  * amountOfFood.value.toDouble() /100
                            _foodFat.value=result.data.fat  * amountOfFood.value.toDouble() /100

                        }
                    }
                }


            }.launchIn(viewModelScope)
        }



    }

    fun onScreenStartGetFood(dietaryLogId:Int){
        viewModelScope.launch {
            getDietaryLogByIdUseCase.invoke(dietaryLogId).onEach {
            result->
                when(result){
                    is Resources.Error ->{}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null){
                            println(result.data)

                            _foodIdOfLog.value=result.data.foodId
                            _amountOfFood.value=result.data.amountOfFood.toString()
                            _dietaryLogId.value=result.data.dietaryLogId
                            _userId.value=result.data.userId
                            _date.value=result.data.date
                            onScreenStart()
                            onAmountOfFoodChange(_amountOfFood.value)
                            onPartOfDayChange(result.data.partOfDay)
                        }

                    }
                }

            }.launchIn(viewModelScope)
        }
    }
    private val _userId = mutableStateOf(0)
    val userId: State<Int> =_userId

    fun onScreenStart(){
         val foodId = foodIdOfLog.value
        viewModelScope.launch {
            getFoodUseCase(foodId).onEach {

                result->
                when(result){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null) {
                            println(result.data)
                            _foodName.value = result.data.foodName
                            _foodCal.value=result.data.cal * amountOfFood.value.toDouble() /100
                            _foodProtein.value=result.data.protein  * amountOfFood.value.toDouble() /100
                            _foodCarb.value=result.data.carb  * amountOfFood.value.toDouble() /100
                            _foodFat.value=result.data.fat  * amountOfFood.value.toDouble() /100

                        }
                    }
                }


            }.launchIn(viewModelScope)
        }
    }

    fun onSubmit(){
        viewModelScope.launch {
            updateDietaryLogUseCaseLB(
                DietaryLogEntity(
                amountOfFood = amountOfFood.value.toDouble(),
                foodId = foodIdOfLog.value,
                partOfDay = partOfDay.value,
                    foodItem = foodName.value,
                    lastEditDate = System.currentTimeMillis(),
                    date = date.value,
                    userId = userId.value,
                    dietaryLogId = dietaryLogId.value


                )
            ).onEach {
                result->
                when(result){
                    is Resources.Error -> {
                        println("err")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null) {

                            _successfulSubmission.value = true



                        }
                            else println("err at succes")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }






}