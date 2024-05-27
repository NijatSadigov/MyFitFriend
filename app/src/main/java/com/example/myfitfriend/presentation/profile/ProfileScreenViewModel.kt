package com.example.myfitfriend.presentation.profile

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.ClearDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.ClearUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.SetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.ClearWorkoutsUseCaseLB
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.domain.use_case.Workout.exercise.GetExercisesUseCase
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
    private val getUserUseCaseLB: GetUserUseCaseLB,
    private val editProfileUserCase: EditProfileUserCase,
    private val clearUserUseCaseLB: ClearUserUseCaseLB,
    private val clearDietaryLogsUseCaseLB: ClearDietaryLogsUseCaseLB,
    private val setUserUseCaseLB: SetUserUseCaseLB,
    private val clearWorkoutsUseCaseLB: ClearWorkoutsUseCaseLB,
    private val clearExercisesUseCase: GetExercisesUseCase
) : ViewModel() {
    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _userWeight = mutableStateOf("")
    val userWeight: State<String> = _userWeight

    private val _userHeight = mutableStateOf("")
    val userHeight: State<String> = _userHeight

    private val _userActivityLevel = mutableIntStateOf(0)
    val userActivityLevel: State<Int> = _userActivityLevel

    private val _userSex = mutableStateOf(false)
    val userSex: State<Boolean> = _userSex

    private val _userAge = mutableIntStateOf(0)
    val userAge: State<Int> = _userAge

    private val _userId = mutableIntStateOf(0)
    private val userId: State<Int> = _userId

    private val _userEmail = mutableStateOf("")
    private val userEmail: State<String> = _userEmail

    private val _userPassword = mutableStateOf("")
    private val userPassword: State<String> = _userPassword

    fun onSexChange(sex: Boolean) {
        _userSex.value = sex
    }

    fun onAgeChange(age: Int) {
        _userAge.value = age
    }

    fun logOut() {
        clearUser()
        clearDietaryLogs()
        clearWorkouts()
        clearExercises()

        sharedPreferences.edit().apply {
            putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL)
            putString(KEY_PASSWORD, NO_PASSWORD)
            apply()
        }
    }

    fun getProfileDetails() {
        viewModelScope.launch {
            getUserUseCaseLB.invoke().onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        println(result.message)
                    }
                    is Resources.Loading -> {
                        // println(result.message)
                    }
                    is Resources.Success -> {
                        result.data?.let { userData ->
                            _userName.value = userData.username
                            _userWeight.value = userData.weight.toString()
                            _userHeight.value = userData.height.toString()
                            _userActivityLevel.value = userData.activityLevel
                            _userAge.value = userData.age
                            _userSex.value = userData.sex
                            _userEmail.value = userData.email
                            _userPassword.value = userData.passwordHash
                            _userId.value = userData.userId
                        } ?: println("No user data available.")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun saveChangesLocally(onSuccess: () -> Unit) {
        viewModelScope.launch {
            setUserUseCaseLB.invoke(
                userEntity = UserEntity(
                    username = userName.value,
                    weight = userWeight.value.toDoubleOrNull() ?: 0.0,
                    height = userHeight.value.toDoubleOrNull() ?: 0.0,
                    activityLevel = userActivityLevel.value,
                    sex = userSex.value,
                    age = userAge.value,
                    email = userEmail.value,
                    userId = userId.value,
                    passwordHash = userPassword.value,
                    isSync = false,
                    lastEditDate = System.currentTimeMillis()
                )
            ).onEach { r ->
                when (r) {
                    is Resources.Error -> {
                        println("Error occurred while saving changes locally.")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        println("Changes saved locally.")
                        saveChanges()
                        onSuccess()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            editProfileUserCase(
                UserEditRequest(
                    username = userName.value,
                    weight = userWeight.value.toDoubleOrNull() ?: 0.0,
                    height = userHeight.value.toDoubleOrNull() ?: 0.0,
                    activityLevel = userActivityLevel.value,
                    sex = userSex.value,
                    age = userAge.value,
                    lastEditDate = System.currentTimeMillis()
                )
            ).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        println(result.data)
                    }
                    is Resources.Loading -> {
                        println(result.data)
                    }
                    is Resources.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateUserName(newName: String) {
        _userName.value = newName
    }

    fun updateWeight(newWeight: String) {
        _userWeight.value = newWeight
    }

    fun updateHeight(newHeight: String) {
        _userHeight.value = newHeight
    }

    fun updateActivityLevel(newLevel: Int) {
        _userActivityLevel.value = newLevel
    }

    private fun clearDietaryLogs() {
        viewModelScope.launch {
            clearDietaryLogsUseCaseLB.invoke().onEach { r ->
                when (r) {
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearUser() {
        viewModelScope.launch {
            clearUserUseCaseLB.invoke().onEach { r ->
                when (r) {
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearWorkouts() {
        viewModelScope.launch {
            clearWorkoutsUseCaseLB.invoke().onEach { r ->
                when (r) {
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearExercises() {
        viewModelScope.launch {
            clearExercisesUseCase.invoke().onEach { r ->
                when (r) {
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }
}
