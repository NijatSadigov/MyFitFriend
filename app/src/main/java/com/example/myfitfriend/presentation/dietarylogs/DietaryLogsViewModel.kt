package com.example.myfitfriend.presentation.dietarylogs

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
@HiltViewModel
class DietaryLogsViewModel
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getDietaryLogsUseCase: GetDietaryLogsUseCase
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


    fun logOut(){
    sharedPreferences.edit().putString(KEY_LOGGED_IN_EMAIL,NO_EMAIL).apply()
    sharedPreferences.edit().putString(KEY_PASSWORD,NO_PASSWORD).apply()

}
    fun getDietaryLogs(){
        viewModelScope.launch {
            getDietaryLogsUseCase().onEach { result->
                when(result){
                    is Resources.Error -> {
                    println("Error: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        _dietaryLogs.value=result.data?: emptyList()
                        _breakfastDietaryLogs.value=separateDietaryLogsByPartOfDay(BREAKFAST_TIME,dietaryLogs.value )
                        _lunchDietaryLogs.value=separateDietaryLogsByPartOfDay(LUNCH_TIME,dietaryLogs.value )
                        _dinnerDietaryLogs.value=separateDietaryLogsByPartOfDay(DINNER_TIME,dietaryLogs.value )
                        _snackDietaryLogs.value=separateDietaryLogsByPartOfDay(SNACK_TIME,dietaryLogs.value )


                    }
                }

            }
        }
    }

    private fun separateDietaryLogsByPartOfDay(partOfDay:Int, allLogs:List<DietaryLogResponse>):
            List<DietaryLogResponse>{

        return allLogs.filter { log -> log.partOfDay == partOfDay }
    }




}