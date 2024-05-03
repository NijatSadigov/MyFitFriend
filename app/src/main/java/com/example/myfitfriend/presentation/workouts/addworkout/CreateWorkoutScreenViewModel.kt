package com.example.myfitfriend.presentation.workouts.addworkout

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.use_case.Workout.CreateWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutScreenViewModel @Inject constructor(
private val createWorkoutUseCase: CreateWorkoutUseCase
) : ViewModel(){
private val _createWorkoutTitle = mutableStateOf("")
val createWorkoutTitle: State<String> = _createWorkoutTitle

    private val _createWorkoutDescription = mutableStateOf("")
    val createWorkoutDescription: State<String> = _createWorkoutDescription

    private val _workoutCreated = mutableStateOf(false)
    val workoutCreated: State<Boolean> = _workoutCreated

    fun onCreateWorkoutDescription(createWorkoutDescription:String){
        _createWorkoutDescription.value=createWorkoutDescription
    }
    fun onCreateWorkoutTitle(createWorkoutTitle:String){
        _createWorkoutTitle.value=createWorkoutTitle
    }

    fun onCreateWorkout() {
        viewModelScope.launch {

            createWorkoutUseCase(WorkoutRequest(createWorkoutTitle.value,createWorkoutDescription.value)).onEach {
                    result->
                when(result){
                    is Resources.Error -> {
                        println(result)

                    }

                    is Resources.Loading -> {

                    }
                    is Resources.Success ->{
                        if(result.data ==200){
                            _workoutCreated.value=true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }



}