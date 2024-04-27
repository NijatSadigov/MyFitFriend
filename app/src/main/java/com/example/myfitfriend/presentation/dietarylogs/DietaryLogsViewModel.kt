package com.example.myfitfriend.presentation.dietarylogs

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
import javax.inject.Inject
import com.example.myfitfriend.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.myfitfriend.util.Constants.NO_EMAIL
import com.example.myfitfriend.util.Constants.KEY_PASSWORD
import com.example.myfitfriend.util.Constants.NO_PASSWORD
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
                    }
                }

            }
        }
    }


}