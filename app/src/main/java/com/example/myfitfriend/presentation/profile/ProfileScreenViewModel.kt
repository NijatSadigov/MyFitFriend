package com.example.myfitfriend.presentation.profile

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.domain.use_case.users.EditProfileUserCase
import com.example.myfitfriend.domain.use_case.users.ProfileUserCase
import com.example.myfitfriend.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.myfitfriend.util.Constants.KEY_PASSWORD
import com.example.myfitfriend.util.Constants.NO_EMAIL
import com.example.myfitfriend.util.Constants.NO_PASSWORD
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val profileUserCase: ProfileUserCase,
    private val editProfileUserCase: EditProfileUserCase
) : ViewModel(){
    private val _userName = mutableStateOf("")
    val userName: State<String> =_userName

    private val _userWeight = mutableDoubleStateOf(0.0)
    val userWeight: State<Double> =_userWeight

    private val _userHeight = mutableDoubleStateOf(0.0)
    val userHeight: State<Double> =_userHeight

    private val _userActivityLevel = mutableIntStateOf(0)
    val userActivityLevel: State<Int> =_userActivityLevel

    fun logOut(){
        sharedPreferences.edit().apply {
            putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL)
            putString(KEY_PASSWORD, NO_PASSWORD)
            apply()
        }
    }



    fun getProfileDetails() {
        viewModelScope.launch {
            profileUserCase().onEach {
                result->

                    when(result){
                        is Resources.Error -> {
                            println(result.message)
                        }
                        is Resources.Loading -> {
                            println(result.message)

                        }
                        is Resources.Success -> {
                            result.data?.let { userData ->
                                _userName.value = userData.username
                                _userWeight.doubleValue = userData.weight
                                _userHeight.doubleValue = userData.height
                                _userActivityLevel.intValue = userData.activityLevel
                            } ?: println("No user data available.")
                        }
                    }

            }.launchIn(viewModelScope)




        }


    }


    fun saveChanges() {
        viewModelScope.launch {

            editProfileUserCase(UserEditRequest(username=userName.value,
                weight = userWeight.value,
                height = userHeight.value,
                activityLevel = userActivityLevel.value
                )).onEach {
                    result->
                    when(result){
                        is Resources.Error -> {
                            println(result.data)
                        }
                        is Resources.Loading -> {
                            println(result.data)


                        }
                        is Resources.Success -> {
                            if(result.data==200){
                                println("done")
                            }
                        }
                    }


            }.launchIn(viewModelScope)




        }


    }









    fun updateUserName(newName: String) {
        _userName.value = newName
    }

    fun updateWeight(newWeight: Double) {
        _userWeight.doubleValue = newWeight
    }

    fun updateHeight(newHeight: Double) {
        _userHeight.doubleValue = newHeight
    }

    fun updateActivityLevel(newLevel: Int) {
        _userActivityLevel.intValue = newLevel
    }

}