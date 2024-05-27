package com.example.myfitfriend.presentation.workouts.spesific

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.domain.use_case.exercise.DeleteExerciseByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.GetExerciseByWorkoutIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.DeleteWorkoutByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutByIdUseCaseLB
import com.example.myfitfriend.domain.use_case.Workout.exercise.DeleteExerciseByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutExercisesByIdUseCase
import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificWorkoutScreenViewModel @Inject constructor(
    private val getWorkoutExercisesByIdUseCase: GetExerciseByWorkoutIdUseCaseLB,
    private val deleteExerciseByIdUseCase: DeleteExerciseByIdUseCaseLB,
    private val insertDeletionTableUseCaseLB:InsertDeletionTableUseCaseLB
) : ViewModel(){
    private val _exercises = mutableStateOf<List<ExerciseEntity>>(emptyList())
    val exercises : State<List<ExerciseEntity>> =_exercises

    private fun insertDeletionEntity(id:Int){
        viewModelScope.launch {
            insertDeletionTableUseCaseLB.invoke(
                DeletedItemsEntity(
                    typeOfItem =2,
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

     fun onDeleteExercise(exerciseId:Int,workoutId: Int,isAdded:Boolean)
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
                        if(result.data!=null ){
                            if(isAdded)
                            insertDeletionEntity(exerciseId)
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
                    //println(exercises)
                    }
                }

            }.launchIn(viewModelScope)

        }
    }
}