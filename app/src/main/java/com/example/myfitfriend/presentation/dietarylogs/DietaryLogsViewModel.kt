package com.example.myfitfriend.presentation.dietarylogs

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogByDateAndPartOfDayUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetFoodUseCase
import com.example.myfitfriend.util.Constants.BREAKFAST_TIME
import com.example.myfitfriend.util.Constants.DINNER_TIME
import javax.inject.Inject
import com.example.myfitfriend.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.myfitfriend.util.Constants.NO_EMAIL
import com.example.myfitfriend.util.Constants.KEY_PASSWORD
import com.example.myfitfriend.util.Constants.LUNCH_TIME
import com.example.myfitfriend.util.Constants.NO_PASSWORD
import com.example.myfitfriend.util.Constants.SNACK_TIME
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class DietaryLogsViewModel
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getDietaryLogsUseCase: GetDietaryLogsUseCase,
    private val getFoodUseCase: GetFoodUseCase,
    private val getDietaryLogByDateAndPartOfDayUseCase: GetDietaryLogByDateAndPartOfDayUseCase
) : ViewModel(){
    private val _dietaryLogs = mutableStateOf<List<DietaryLogResponse>>(emptyList())
    val dietaryLogs: State<List<DietaryLogResponse>> =_dietaryLogs

    private val _breakfastDietaryLogs = mutableStateOf<List<DietaryLogResponse>>(emptyList())
    val breakfastDietaryLogs: State<List<DietaryLogResponse>> = _breakfastDietaryLogs

    private val _lunchDietaryLogs = mutableStateOf<List<DietaryLogResponse>>(emptyList())
    val lunchDietaryLogs: State<List<DietaryLogResponse>> = _lunchDietaryLogs

    private val _dinnerDietaryLogs = mutableStateOf<List<DietaryLogResponse>>(emptyList())
    val dinnerDietaryLogs: State<List<DietaryLogResponse>> = _dinnerDietaryLogs

    private val _snackDietaryLogs = mutableStateOf<List<DietaryLogResponse>>(emptyList())
    val snackDietaryLogs: State<List<DietaryLogResponse>> = _snackDietaryLogs

    private val _date = mutableStateOf(LocalDate.now().toString())
    val date : State<String> =_date



    private val _totalCalories = mutableStateOf(0.0)
    val totalCalories: State<Double> = _totalCalories

    private val _totalCarbs = mutableStateOf(0.0)
    val totalCarbs: State<Double> = _totalCarbs

    private val _totalFats = mutableStateOf(0.0)
    val totalFats: State<Double> = _totalFats

    private val _totalProtein = mutableStateOf(0.0)
    val totalProtein: State<Double> = _totalProtein

    private val _totalBreakfastCalories = mutableStateOf(0.0)
    val totalBreakfastCalories: State<Double> = _totalBreakfastCalories
    private val _totalLunchCalories = mutableStateOf(0.0)
    val totalLunchCalories: State<Double> = _totalLunchCalories
    private val _totalDinnerCalories = mutableStateOf(0.0)
    val totalDinnerCalories: State<Double> = _totalDinnerCalories
    private val _totalSnackCalories = mutableStateOf(0.0)
    val totalSnackCalories: State<Double> = _totalSnackCalories

    fun logOut(){
        sharedPreferences.edit().apply {
            putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL)
            putString(KEY_PASSWORD, NO_PASSWORD)
            apply()
        }
}
    fun getDietaryLogs(){
        viewModelScope.launch {
            println("Launcs")
            getDietaryLogsUseCase().onEach { result->
                when(result){
                    is Resources.Error -> {
                    println("Error: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {

                        val todayLogs = result.data?.filter {
                            it.date == _date.value
                        } ?: emptyList()

                        _dietaryLogs.value = todayLogs
                        println("success")
                        println(_dietaryLogs.value)
                        getDietaryLogsForPartyOfDay(0)
                        getDietaryLogsForPartyOfDay(1)
                        getDietaryLogsForPartyOfDay(2)
                        getDietaryLogsForPartyOfDay(3)
                        calculateTotalMacros()



                    }
                }

            }.launchIn(viewModelScope)



        }
    }

    private fun getDietaryLogsForPartyOfDay( partOfDay: Int ){
        viewModelScope.launch {
            getDietaryLogByDateAndPartOfDayUseCase(date.value,partOfDay).onEach{
                result->
                when(result){
                    is Resources.Error -> {
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        println("getDietaryLogsForPartyOfDay")
                        println(result.data)
                        if(partOfDay==0)
                            _breakfastDietaryLogs.value=result.data?: emptyList()
                        else if(partOfDay==1){
                            _lunchDietaryLogs.value=result.data?: emptyList()
                        }
                        else if(partOfDay==2){
                            _dinnerDietaryLogs.value=result.data?: emptyList()

                        }
                        else {
                            _snackDietaryLogs.value=result.data?: emptyList()


                        }
                    }
                }

            }.launchIn(viewModelScope)
        }

    }


    private fun calculateTotalMacros() {
        _totalCalories.value = 0.0  // Reset before calculation
        _totalBreakfastCalories.value=0.0
        _totalLunchCalories.value=0.0
        _totalDinnerCalories.value=0.0
        _totalSnackCalories.value=0.0

        viewModelScope.launch {
            dietaryLogs.value.forEach { log ->
                getFoodUseCase(log.foodId).collect { result ->
                    when (result) {
                        is Resources.Error -> Unit
                        is Resources.Loading -> Unit
                        is Resources.Success -> {
                            val currentCalories = result.data?.cal ?: 0.0
                            val currentCarbs= result.data?.carb?:0.0
                            val currentProtein= result.data?.protein?:0.0
                            val currentFat= result.data?.fat?:0.0

                            _totalCalories.value += currentCalories *log.amountOfFood/100.0
                            _totalCarbs.value += currentCarbs * log.amountOfFood/100
                            _totalProtein.value += currentProtein * log.amountOfFood/100
                            _totalFats.value += currentFat * log.amountOfFood/100





                        }
                    }
                }
            }

            breakfastDietaryLogs.value.forEach { log ->
                getFoodUseCase(log.foodId).collect { result ->
                    when (result) {
                        is Resources.Error -> Unit
                        is Resources.Loading -> Unit
                        is Resources.Success -> {
                            val currentCalories = result.data?.cal ?: 0.0


                            _totalBreakfastCalories.value += currentCalories *log.amountOfFood/100.0






                        }
                    }
                }
            }

            lunchDietaryLogs.value.forEach { log ->
                getFoodUseCase(log.foodId).collect { result ->
                    when (result) {
                        is Resources.Error -> Unit
                        is Resources.Loading -> Unit
                        is Resources.Success -> {
                            val currentCalories = result.data?.cal ?: 0.0


                            _totalLunchCalories.value += currentCalories *log.amountOfFood/100.0






                        }
                    }
                }
            }

            dinnerDietaryLogs.value.forEach { log ->
                getFoodUseCase(log.foodId).collect { result ->
                    when (result) {
                        is Resources.Error -> Unit
                        is Resources.Loading -> Unit
                        is Resources.Success -> {
                            val currentCalories = result.data?.cal ?: 0.0


                            _totalDinnerCalories.value += currentCalories *log.amountOfFood/100.0






                        }
                    }
                }
            }


            snackDietaryLogs.value.forEach { log ->
                getFoodUseCase(log.foodId).collect { result ->
                    when (result) {
                        is Resources.Error -> Unit
                        is Resources.Loading -> Unit
                        is Resources.Success -> {
                            val currentCalories = result.data?.cal ?: 0.0


                            _totalSnackCalories.value += currentCalories *log.amountOfFood/100.0






                        }
                    }
                }
            }


        }
    }

    private fun separateDietaryLogsByPartOfDay(partOfDay:Int,date:String, allLogs:List<DietaryLogResponse>):
            List<DietaryLogResponse>{

        return allLogs.filter { log -> log.partOfDay == partOfDay && log.date == date }
    }




}