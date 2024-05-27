package com.example.myfitfriend.presentation.workouts.editworkout


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.UpdateWorkoutUseCaseLB
import com.example.myfitfriend.domain.use_case.Workout.EditWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutByIdUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditWorkoutScreenViewModel @Inject constructor(
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCaseLB,
    private val editWorkoutUseCase: UpdateWorkoutUseCaseLB
) : ViewModel(){
    private val _editWorkoutTitle = mutableStateOf("")
    val editWorkoutTitle: State<String> = _editWorkoutTitle

    private val _editWorkoutDate = mutableStateOf("")
    val editWorkoutDate: State<String> = _editWorkoutDate

    private val _currentWorkout= mutableStateOf<WorkoutEntity?>(null)
    val currentWorkout :State<WorkoutEntity?> =_currentWorkout

    private val _editWorkoutDescription = mutableStateOf("")
    val editWorkoutDescription: State<String> = _editWorkoutDescription

    private val _workoutCreated = mutableStateOf(false)
    val workoutCreated: State<Boolean> = _workoutCreated

    fun onEditWorkoutDescription(editWorkoutDescription:String){
        _editWorkoutDescription.value=editWorkoutDescription
    }
    fun onEditWorkoutTitle(editWorkoutTitle:String){
        _editWorkoutTitle.value=editWorkoutTitle
    }
    fun getCurrentWorkoutDetails(workoutId: Int){
        viewModelScope.launch {

            getWorkoutByIdUseCase(workoutId = workoutId).onEach {
                    result->
                when(result){
                    is Resources.Error -> {
                        println(result)

                    }

                    is Resources.Loading -> {

                    }
                    is Resources.Success ->{
                        _editWorkoutTitle.value=result.data!!.title
                        _editWorkoutDescription.value=result.data.description
                        _editWorkoutDate.value=result.data.date
                        _currentWorkout.value=result.data
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onEditWorkout(workoutId:Int) {
        viewModelScope.launch {

            editWorkoutUseCase(
                WorkoutEntity(
                    title = editWorkoutTitle.value,
                    description = editWorkoutDescription.value,
                    lastEditDate = System.currentTimeMillis(),
                    userId = currentWorkout.value?.userId?:0,
                    date=currentWorkout.value?.date?:"",
                    workoutId = workoutId
                    )
            ).onEach {
                    result->
                when(result){
                    is Resources.Error -> {
                        println(result)

                    }

                    is Resources.Loading -> {

                    }
                    is Resources.Success ->{
                        if(result.data !=null && result.data>0){
                            _workoutCreated.value=true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }


}