package com.example.myfitfriend.presentation.workouts.exercises.addexercise

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.data.model.Exercise
import com.MyFitFriend.requests.ExerciseRequest
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.domain.use_case.exercise.CreateExerciseUseCaseLB
import com.example.myfitfriend.domain.use_case.Workout.exercise.DeleteExerciseByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutExercisesByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.AddExerciseToWorkoutUseCase
import com.example.myfitfriend.util.Resources
import com.example.myfitfriend.util.SyncOperationsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddExerciseScreenViewModel @Inject constructor(
    private val addExerciseToWorkoutUseCase: CreateExerciseUseCaseLB,
) : ViewModel() {
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _weights = mutableStateOf("")
    val weights: State<String> = _weights

    private val _setCount = mutableStateOf("")
    val setCount: State<String> = _setCount

    private val _repCount = mutableStateOf("")
    val repCount: State<String> = _repCount

    private val _restTime = mutableStateOf("")
    val restTime: State<String> = _restTime

    private val _onSuccessfullyAdd = mutableStateOf(false)
    val onSuccessfullyAdd: State<Boolean> = _onSuccessfullyAdd

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun onDescriptionChanged(description: String) {
        _description.value = description
    }

    fun onWeightsChanged(weights: String) {
        _weights.value = weights
    }

    fun onRepCountChanged(repCount: String) {
        _repCount.value = repCount
    }

    fun onSetCountChanged(setCount: String) {
        _setCount.value = setCount
    }

    fun onRestTimeChanged(restTime: String) {
        _restTime.value = restTime
    }

    fun onSubmitNewExercise(workoutId: Int) {
        viewModelScope.launch {
            addExerciseToWorkoutUseCase(
                ee = ExerciseEntity(
                    title = title.value,
                    description = description.value,
                    weights = weights.value.toDoubleOrNull() ?: 0.0,
                    setCount = setCount.value.toIntOrNull() ?: 0,
                    repCount = repCount.value.toIntOrNull() ?: 0,
                    restTime = restTime.value.toDoubleOrNull() ?: 0.0,
                    lastEditDate = System.currentTimeMillis(),
                    workoutId = workoutId
                )
            ).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        println("Error happened: $result")
                    }
                    is Resources.Loading -> {
                    }
                    is Resources.Success -> {
                        if (result.data != null) {
                            _onSuccessfullyAdd.value = true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
