package com.example.myfitfriend.presentation.workouts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.workout.DeleteWorkoutByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutsUseCaseLB

import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.data.remote.requests.UserRegisterRequest
import com.example.myfitfriend.domain.use_case.Workout.DeleteWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
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
    private val workoutsUseCase: GetWorkoutsUseCaseLB,
    private val deleteWorkoutByIdUseCase: DeleteWorkoutByIdUseCaseLB,
    private val insertDeletionTableUseCaseLB:InsertDeletionTableUseCaseLB
) :ViewModel(){

    private val _workouts = MutableStateFlow<List<WorkoutEntity>>(emptyList())
        //val workouts: State<List<Workout>> =_workouts
        val workouts: StateFlow<List<WorkoutEntity>> = _workouts.asStateFlow()

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
    private fun insertDeletionEntity(id:Int){
        viewModelScope.launch {
            insertDeletionTableUseCaseLB.invoke(
                DeletedItemsEntity(
                typeOfItem =1,
                deletedId =id
            )
            ).onEach {
                    r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {


                    }
                }
            } .launchIn(viewModelScope)
        }

    }


    fun onDelete(workoutId:Int, isAdded : Boolean){
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
                        if(result.data!=null) {
                            if(isAdded) {
                                insertDeletionEntity(workoutId)
                            }
                            getWorkouts()
                        }
                    }
                }

            }.launchIn(viewModelScope)

        }
    }
}