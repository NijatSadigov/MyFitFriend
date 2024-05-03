package com.example.myfitfriend.presentation.workouts.exercises.editexercise

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.requests.ExerciseRequest
import com.example.myfitfriend.domain.use_case.Workout.GetExerciseByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.AddExerciseToWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.UpdateExerciseUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExerciseScreenViewModel @Inject constructor(
    private val  updateExerciseUseCase: UpdateExerciseUseCase,
    private val getExerciseUseCase: GetExerciseByIdUseCase
    ) : ViewModel(){


    private val _title = mutableStateOf("")
    val title: State<String> =_title
    private val _description = mutableStateOf("")
    val description: State<String> =_description
    private val _weights = mutableStateOf("")
    val weights: State<String> =_weights
    private val _setCount = mutableStateOf("")
    val setCount: State<String> =_setCount
    private val _repCount = mutableStateOf("")
    val repCount: State<String> =_repCount
    private val _restTime = mutableStateOf("")
    val restTime: State<String> =_restTime

    private val _onSuccessfullyEdit = mutableStateOf(false)
    val onSuccessfullyEdit: State<Boolean> =_onSuccessfullyEdit



    fun onTitleChanged(title:String){
        _title.value=title
    }
    fun onDescriptionChanged(description:String){
        _description.value=description
    }
    fun onWeightsChanged(weights:String){
        _weights.value=weights
    }
    fun onRepCountChanged(repCount:String){
        _repCount.value=repCount
    }

    fun onSetCountChanged(setCount:String){
        _setCount.value=setCount
    }
    fun onRestTimeChanged(restTime:String){
        _restTime.value=restTime
    }
    fun getCurrentDetails(exerciseId: Int){
        viewModelScope.launch {
            getExerciseUseCase(
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
                        println(result.data)
                        if(result.data!=null){
                        _title.value=result.data.title
                            _description.value=result.data.description
                            _weights.value=result.data.weights.toString()
                            _repCount.value=result.data.repCount.toString()
                            _setCount.value=result.data.setCount.toString()
                            _restTime.value=result.data.restTime.toString()
                        }
                    }
                }


            }.launchIn(viewModelScope)


        }
    }

    fun onSubmitEditedExercise(workoutId: Int,exerciseId:Int){
        viewModelScope.launch {
            updateExerciseUseCase(
                workoutId ,exerciseId, exerciseRequest = ExerciseRequest(
                    title = title.value,
                    description = description.value,
                    weights = weights.value.toDouble(),
                    setCount=setCount.value.toInt(),
                    repCount = repCount.value.toInt(),
                    restTime = restTime.value.toDouble()
                )
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
                            _onSuccessfullyEdit.value=true                        }
                    }
                }

            }.launchIn(viewModelScope)

        }
    }


}