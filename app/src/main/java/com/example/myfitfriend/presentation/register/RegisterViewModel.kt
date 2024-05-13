package com.example.myfitfriend.presentation.register

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest
import com.example.myfitfriend.domain.use_case.users.RegisterUserCase
import com.example.myfitfriend.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.myfitfriend.util.Constants.KEY_PASSWORD
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val registerUserCase: RegisterUserCase
) :ViewModel()
{
    private val _emailState = mutableStateOf("")
    val emailState: State<String> =_emailState

    private val _passwordState = mutableStateOf("")
    val passwordState: State<String> =_passwordState

    private val _userName = mutableStateOf("")
    val userName: State<String> =_userName

    private val _userActivityLevel = mutableIntStateOf(0)
    val userActivityLevel: State<Int> =_userActivityLevel

    private val _userWeight = mutableStateOf("0.0") // Initial value set to "0.0" instead of ""
    val userWeight: State<String> = _userWeight

    private val _userHeight = mutableStateOf("0.0") // Initial value set to "0.0" instead of ""
    val userHeight: State<String> = _userHeight

    private val _userAge = mutableStateOf(0) // Assuming 18 as a default age
    val userAge: State<Int> = _userAge

    private val _isRegistered = mutableStateOf(false)
    val isRegistered: State<Boolean> =_isRegistered

    private val _userSex = mutableStateOf(false)
    val userSex: State<Boolean> =_userSex

    ///CHANGES

    fun onEmailChange(email:String){
        _emailState.value=email
    }
    fun onPasswordChange(password:String){
        _passwordState.value=password
    }
    fun onUserNameChange(userName:String){
        _userName.value=userName
    }
    fun onUserWeightChange(userWeight:String){

        _userWeight.value=userWeight

    }
    fun onUserHeightChange(userHeight:String){

        _userHeight.value = userHeight

    }

    fun onUserActivityLevelChange(userActivity:Int){

            _userActivityLevel.intValue=userActivity

    }
    fun onSexChange(sex:Boolean){
        _userSex.value=sex
    }
    fun onAgeChange(age:Int){
        _userAge.value=age
    }
    fun onRegister(){
        viewModelScope.launch {
            val safeWeight = userWeight.value.toDoubleOrNull() ?: 0.0
            val safeHeight = userHeight.value.toDoubleOrNull() ?: 0.0
            val safeActivityLevel = userActivityLevel.value//.toIntOrNull() ?: 0


            registerUserCase(UserRegisterRequest(
                email =emailState.value,
                passwordHash = passwordState.value,
                username=userName.value,
                activityLevel = safeActivityLevel,
                height = safeHeight,
                weight = safeWeight,
                age = userAge.value,
                sex=userSex.value
                )).onEach {
                    result->

                when(result){
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data==200)
                            successfullyRegistered()
                    }
                }

            }.launchIn(viewModelScope)

        } }
    private fun authAPI(email: String,password: String){
        basicAuthInterceptor.email=email
        basicAuthInterceptor.password=password

    }
    private fun successfullyRegistered(){
        _isRegistered.value=true
        sharedPreferences.edit().putString(KEY_LOGGED_IN_EMAIL , emailState.value).apply()
        sharedPreferences.edit().putString(KEY_PASSWORD , passwordState.value).apply()

        authAPI(emailState.value,passwordState.value)
    }



}