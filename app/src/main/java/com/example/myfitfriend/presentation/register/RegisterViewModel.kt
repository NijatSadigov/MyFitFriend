package com.example.myfitfriend.presentation.register

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
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
) : ViewModel()
{

    private val _emailState = mutableStateOf("")
    val emailState: State<String> = _emailState

    private val _passwordState = mutableStateOf("")
    val passwordState: State<String> = _passwordState

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _userActivityLevel = mutableIntStateOf(0)
    val userActivityLevel: State<Int> = _userActivityLevel

    private val _userWeight = mutableStateOf("")
    val userWeight: State<String> = _userWeight

    private val _userHeight = mutableStateOf("")
    val userHeight: State<String> = _userHeight

    private val _userAge = mutableStateOf(0)
    val userAge: State<Int> = _userAge

    private val _isRegistered = mutableStateOf(false)
    val isRegistered: State<Boolean> = _isRegistered

    private val _userSex = mutableStateOf(false)
    val userSex: State<Boolean> = _userSex

    // Derived state to check if all fields are filled
    val isRegisterButtonEnabled: State<Boolean> = derivedStateOf {
        _emailState.value.isNotBlank() &&
                _passwordState.value.isNotBlank() &&
                _userName.value.isNotBlank() &&
                _userWeight.value.isNotBlank() &&
                _userHeight.value.isNotBlank() &&
                _userAge.value > 0
    }

    fun onEmailChange(email: String) {
        _emailState.value = email
    }

    fun onPasswordChange(password: String) {
        _passwordState.value = password
    }

    fun onUserNameChange(userName: String) {
        _userName.value = userName
    }

    fun onUserWeightChange(userWeight: String) {
        _userWeight.value = userWeight
    }

    fun onUserHeightChange(userHeight: String) {
        _userHeight.value = userHeight
    }

    fun onUserActivityLevelChange(userActivity: Int) {
        _userActivityLevel.intValue = userActivity
    }

    fun onSexChange(sex: Boolean) {
        _userSex.value = sex
    }

    fun onAgeChange(age: Int) {
        _userAge.value = age
    }

    fun onRegister(context: Context) {
        viewModelScope.launch {
            val safeWeight = userWeight.value.toDoubleOrNull() ?: 0.0
            val safeHeight = userHeight.value.toDoubleOrNull() ?: 0.0
            val safeActivityLevel = userActivityLevel.value

            registerUserCase(UserRegisterRequest(
                email = emailState.value,
                passwordHash = passwordState.value,
                username = userName.value,
                activityLevel = safeActivityLevel,
                height = safeHeight,
                weight = safeWeight,
                age = userAge.value,
                sex = userSex.value,
                lastEditDate = System.currentTimeMillis()
            )).onEach { result ->
                when (result) {
                    is Resources.Error -> {

                    }
                    is Resources.Loading -> { }
                    is Resources.Success -> {
                        when (result.data) {
                            409 -> showToast(context, "Email already used")
                            403 -> showToast(context, "Email format is wrong")
                            400 -> showToast(context, "An error occurred")
                        }
                        if (result.data == 200)
                            successfullyRegistered(context)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun successfullyRegistered(context: Context) {
        _isRegistered.value = true

        showToast(context, "successfully registered")
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
