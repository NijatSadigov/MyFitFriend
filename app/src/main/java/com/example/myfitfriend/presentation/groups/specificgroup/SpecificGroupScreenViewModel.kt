package com.example.myfitfriend.presentation.groups.specificgroup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.data.remote.reponses.Nutritions
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.use_case.Workout.DeleteWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.domain.use_case.groups.mainscreen.GetGroupsUseCase
import com.example.myfitfriend.domain.use_case.groups.specific.GetGroupMembersByGroupIdUseCase
import com.example.myfitfriend.domain.use_case.users.GetUserDetailsByIdUseCase
import com.example.myfitfriend.domain.use_case.users.ProfileUserCase
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
class SpecificGroupScreenViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getUserDetailsByIdUseCase: GetUserDetailsByIdUseCase,
    private val getGroupMembersByGroupIdUseCase: GetGroupMembersByGroupIdUseCase
) : ViewModel(){
    private val _dietGroups = mutableStateOf<List<DietGroup>>(emptyList())
    val dietGroups : State<List <DietGroup>> = _dietGroups

    private val _groupName = mutableStateOf<List<String>>(emptyList())
    val groupName: State<List<String>> = _groupName


    private val _groupOwnerName= mutableStateOf("")
    val groupOwnerName:State<String> =_groupOwnerName

    private val _membersId = mutableStateOf<List<Int>>(emptyList())
    val membersId: State<List<Int>> = _membersId

    private val _memberLogs = mutableStateOf<List<Nutritions>>(emptyList())
    val memberLogs: State<List<Nutritions>> =_memberLogs


    fun getGroupMembers(groupId:Int){
        viewModelScope.launch {
            getGroupMembersByGroupIdUseCase.invoke(groupId).onEach {
                result->
                when(result){
                    is Resources.Error ->    println("Error: ${result.data} , ${result.message}")
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null)
                            _membersId.value=result.data
                    }
                }



            }.launchIn(viewModelScope)



        }
    }

    fun getUsername(ownerId:Int){



        viewModelScope.launch {

            getUserDetailsByIdUseCase.invoke(ownerId).onEach {
                result->
                when(result){
                    is Resources.Error -> {
                        println("Error: ${result.data} , ${result.message}")

                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        _groupOwnerName.value=result.data!!.username
                    }
                }
            }.launchIn(viewModelScope)



        }





    }

}