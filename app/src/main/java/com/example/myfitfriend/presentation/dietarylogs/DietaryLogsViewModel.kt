package com.example.myfitfriend.presentation.dietarylogs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogsByDateAndIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogByDateAndPartOfDayUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
import javax.inject.Inject
import com.example.myfitfriend.util.Resources
import com.example.myfitfriend.util.SyncOperationsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class DietaryLogsViewModel
@Inject constructor(
    private val getDietaryLogsUseCaseLB: GetDietaryLogsUseCaseLB,
    private val getFoodByUseCaseLB: GetFoodByIdUseCaseLB,
    private val getDietaryLogByDateAndPartOfDayUseCase: GetDietaryLogsByDateAndIdUseCaseLB,
    private val syncOperationsUtil: SyncOperationsUtil
) : ViewModel(){

    private val _dietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    val dietaryLogs: State<List<DietaryLogEntity>> =_dietaryLogs

    private val _breakfastDietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    val breakfastDietaryLogs: State<List<DietaryLogEntity>> = _breakfastDietaryLogs

    private val _lunchDietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    val lunchDietaryLogs: State<List<DietaryLogEntity>> = _lunchDietaryLogs

    private val _dinnerDietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    val dinnerDietaryLogs: State<List<DietaryLogEntity>> = _dinnerDietaryLogs

    private val _snackDietaryLogs = mutableStateOf<List<DietaryLogEntity>>(emptyList())
    val snackDietaryLogs: State<List<DietaryLogEntity>> = _snackDietaryLogs

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

    val _connectionStatus=  mutableStateOf(ConnectivityObserver.Status.Unavailable)
    val connectionStatus:State<ConnectivityObserver.Status> =_connectionStatus

    fun setConnectionState(state: ConnectivityObserver.Status) {
        _connectionStatus.value = state
        println("state $state")
        if (connectionStatus.value == ConnectivityObserver.Status.Available) {
            viewModelScope.launch {
                // Start sync deletions and wait for it to complete
                val job1=syncOperationsUtil.syncProfileDetails(viewModelScope)
                job1.join()
                val job2 =syncOperationsUtil.syncDeletions(scope = viewModelScope)
                job2.join()
                // Start sync dietary logs and sync workouts in parallel
                val dietaryLogsJob = syncOperationsUtil.syncDietaryLogs(viewModelScope)
                val workoutsJob = syncOperationsUtil.syncWorkouts(viewModelScope)

                // Wait for both to complete
                dietaryLogsJob.join()
                workoutsJob.join()

                // Fetch dietary logs after sync is complete
                getDietaryLogs().join()
            }
        }
    }


    fun getDietaryLogs():Job{
       return  viewModelScope.launch {
            //println("Launcs")
            getDietaryLogsUseCaseLB().onEach { result->
                when(result){
                    is Resources.Error -> {
                    println("Error: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        //println("result data ${result.data}")
                        val todayLogs = result.data?.filter {
                            it.date == _date.value
                        } ?: emptyList()
                      //  println("todaylogs $todayLogs")
                        _dietaryLogs.value = todayLogs
                        //println("success")
                        //println(_dietaryLogs.value)
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
                        //println("getDietaryLogsForPartyOfDay")
                        //println(result.data)
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
                getFoodByUseCaseLB(log.foodId).collect { result ->
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
                getFoodByUseCaseLB(log.foodId).collect { result ->
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
                getFoodByUseCaseLB(log.foodId).collect { result ->
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
                getFoodByUseCaseLB(log.foodId).collect { result ->
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
                getFoodByUseCaseLB(log.foodId).collect { result ->
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





}