package com.example.myfitfriend.presentation.workouts.spesific

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.domain.use_case.Workout.exercise.DeleteExerciseByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutExercisesByIdUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificWorkoutScreenViewModel @Inject constructor(
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getWorkoutExercisesByIdUseCase: GetWorkoutExercisesByIdUseCase,
    private val deleteExerciseByIdUseCase: DeleteExerciseByIdUseCase
) : ViewModel(){
    private val _exercises = mutableStateOf<List<Exercise>>(emptyList<Exercise>())
    val exercises : State<List<Exercise>> =_exercises


     fun onDeleteExercise(exerciseId:Int,workoutId: Int)
    {
        viewModelScope.launch {
            deleteExerciseByIdUseCase(
                exerciseId
            ).onEach {
                    result->

                when(result){
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data==200){
                            getExercises(workoutId)
                        }
                    }
                }

            }.launchIn(viewModelScope)

        }
    }

    fun getExercises(workoutId:Int){
        viewModelScope.launch {
            getWorkoutExercisesByIdUseCase(
                workoutId
            ).onEach {
                    result->

                when(result){
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        _exercises.value = result.data ?: emptyList() // Safely assign with fallback to empty list
                    println(exercises)
                    }
                }

            }.launchIn(viewModelScope)

        }
    }
}