package com.example.myfitfriend.presentation.workouts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest
import com.example.myfitfriend.domain.use_case.Workout.DeleteWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.util.Resources

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(
    private val workoutsUseCase: GetWorkoutsUseCase,
    private val deleteWorkoutByIdUseCase: DeleteWorkoutByIdUseCase
) :ViewModel(){

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
        //val workouts: State<List<Workout>> =_workouts
        val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    fun getWorkouts(){
        viewModelScope.launch {
            workoutsUseCase(

            ).onEach {
                    result->

                when(result){
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        _workouts.value = result.data ?: emptyList() // Safely assign with fallback to empty list
                    }
                }

            }.launchIn(viewModelScope)

        }
    }

    fun onDelete(workoutId:Int){
        viewModelScope.launch {
            deleteWorkoutByIdUseCase(
                workoutId            ).onEach {
                    result->

                when(result){
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data==200){
                            getWorkouts()
                        }
                    }
                }

            }.launchIn(viewModelScope)

        }
    }
}