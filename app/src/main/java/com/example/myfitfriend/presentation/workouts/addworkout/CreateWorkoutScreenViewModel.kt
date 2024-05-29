package com.example.myfitfriend.presentation.workouts.addworkout

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.CreateWorkoutEntityUseCaseLB
import com.example.myfitfriend.data.remote.requests.UserLoginRequest
import com.example.myfitfriend.domain.use_case.Workout.CreateWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutScreenViewModel @Inject constructor(
private val createWorkoutUseCase: CreateWorkoutEntityUseCaseLB,
    private val getUserUseCaseLB: GetUserUseCaseLB,
) : ViewModel(){
private val _createWorkoutTitle = mutableStateOf("")
val createWorkoutTitle: State<String> = _createWorkoutTitle

    private val _createWorkoutDescription = mutableStateOf("")
    val createWorkoutDescription: State<String> = _createWorkoutDescription

    private val _workoutCreated = mutableStateOf(false)
    val workoutCreated: State<Boolean> = _workoutCreated

    private val _userId = mutableStateOf(0)
    val userId:State<Int> =_userId

    fun onCreateWorkoutDescription(createWorkoutDescription:String){
        _createWorkoutDescription.value=createWorkoutDescription
    }
    fun onCreateWorkoutTitle(createWorkoutTitle:String){
        _createWorkoutTitle.value=createWorkoutTitle
    }


    fun onScreenStart(){
        viewModelScope.launch {
            getUserUseCaseLB.invoke().onEach {
                r->
                when(r){
                    is Resources.Error -> {
                        println("error at getting userId:${r.data}")

                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        println("user data ${r.data}")
                        _userId.value=r.data?.userId ?:0
                    }
                }
            }
        }

    }


    fun onCreateWorkout() {
        viewModelScope.launch {

            createWorkoutUseCase(
                WorkoutEntity(title =createWorkoutTitle.value,
                   description =createWorkoutDescription.value,
                    lastEditDate = System.currentTimeMillis(),
                    date = LocalDate.now().toString(),
                    userId=_userId.value
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
                        println("created workout ${result.data}")
                        if(result.data !=null && result.data>0 && userId.value==0){
                            _workoutCreated.value=true
                        }
                        else {
                            println("err occurred at onCreateWorkout")
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }



}