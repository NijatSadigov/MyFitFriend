package com.example.myfitfriend.presentation.login

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.asDietaryLogEntity
import com.example.myfitfriend.data.local.asExerciseEntity
import com.example.myfitfriend.data.local.asLocalFood
import com.example.myfitfriend.data.local.asUser
import com.example.myfitfriend.data.local.asUserEntity
import com.example.myfitfriend.data.local.asWorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.SetDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.SetExercisesUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.SetFoodsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.ClearUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.SetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.SetWorkoutsUseCaseLB
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.GetExercisesUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.ShowFoodsUseCase
import com.example.myfitfriend.domain.use_case.users.GetUserDetailsByIdUseCase
import com.example.myfitfriend.domain.use_case.users.LoginUserCase
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
class LoginViewModel @Inject constructor(
    private val loginUserCase: LoginUserCase,
    private val sharedPreferences: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,

    private val profileUserCase: ProfileUserCase,
    private val setUserUseCaseLB: SetUserUseCaseLB,

    private val getFoodsUseCase:ShowFoodsUseCase,
    private val setFoodsUseCaseLB: SetFoodsUseCaseLB,
    private val getDietaryLogsUseCase: GetDietaryLogsUseCase,
    private val setDietaryLogsUseCaseLB: SetDietaryLogsUseCaseLB,

    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val setWorkoutsUseCaseLB: SetWorkoutsUseCaseLB,

    private val getExercises:GetExercisesUseCase,
    private val setExercisesUseCaseLB: SetExercisesUseCaseLB

) : ViewModel() {
    private val _emailState = mutableStateOf("")
    val emailState: State<String> = _emailState

    private val _passwordState = mutableStateOf("")
    val passwordState: State<String> = _passwordState

    private val _logInState = mutableStateOf(false)
    val logInState: State<Boolean> = _logInState

    private val _isButtonEnabled = mutableStateOf(false)
    val isButtonEnabled: State<Boolean> = _isButtonEnabled

     val _toastEvent = mutableStateOf<ToastEvent?>(null)
    val toastEvent: State<ToastEvent?> = _toastEvent

    fun onEmailChange(email: String) {
        _emailState.value = email
        checkFields()
    }

    fun onPasswordChange(password: String) {
        _passwordState.value = password
        checkFields()
    }

    private fun checkFields() {
        _isButtonEnabled.value = emailState.value.isNotEmpty() && passwordState.value.isNotEmpty()
    }

    fun logIn() {
        viewModelScope.launch {
            loginUserCase(UserLoginRequest(emailState.value, passwordState.value)).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        _logInState.value = false

                        //println("result $result")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        //println("return code: ${result.data}   : @${result.message}")
                        if (result.data == 200) {
                            //println("success")
                            successfullyLoggedIn()
                        }
                        if (result.data == 401) {
                            //println("true")
                            _toastEvent.value = ToastEvent.ShowToast("Wrong password or email")
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun authAPI(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    private fun successfullyLoggedIn() {
        sharedPreferences.edit().putString(KEY_LOGGED_IN_EMAIL, emailState.value).apply()
        sharedPreferences.edit().putString(KEY_PASSWORD, passwordState.value).apply()

        authAPI(emailState.value, passwordState.value)
       //get user ->set user | setFoods|setDietaryLogs

        getUserFromServer()
        getFoods()


        _logInState.value = true


    }



    fun isLoggedIn() {
        val currentEmail = sharedPreferences.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val currentPassword = sharedPreferences.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        if (currentEmail != NO_EMAIL && currentPassword != NO_PASSWORD) {
            authAPI(currentEmail, currentPassword)
            _logInState.value = true
        } else {
            _logInState.value = false
        }
    }

    private fun getUserFromServer(){
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
                        if(result.data!=null){
                            val userEntity= result.data
                            setUserToLB(userEntity)


                        }

                    }
                }

            }.launchIn(viewModelScope)




        }
    }

    private fun setUserToLB(userEntity: UserEntity){
        viewModelScope.launch {
            setUserUseCaseLB.invoke(userEntity).onEach {
                r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun getFoods(){
        viewModelScope.launch {
            getFoodsUseCase.invoke().onEach {
                    r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(r.data!=null){
                            val localFoods=  r.data.map { it.asLocalFood() }

                            setFoods(localFoods)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


   private fun setFoods(foods :List<LocalFoodEntity>){
        viewModelScope.launch {
            setFoodsUseCaseLB.invoke(foods).onEach {
                    r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {

                    }
                }
            }.launchIn(viewModelScope)
        }

   }


    //get DietaryLogs and set




}






sealed class ToastEvent {
    data class ShowToast(val message: String) : ToastEvent()
}


