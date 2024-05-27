package com.example.myfitfriend.presentation.groups.mainscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfitfriend.MainActivity
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.domain.use_case.Workout.DeleteWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.domain.use_case.groups.delete.DeleteDietGroupUseCase
import com.example.myfitfriend.domain.use_case.groups.mainscreen.GetGroupsUseCase
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
class GroupsScreenViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val deleteDietGroupUseCase: DeleteDietGroupUseCase
) : ViewModel() {
    private val _dietGroups = mutableStateOf<List<DietGroup>>(emptyList())
    val dietGroups: State<List<DietGroup>> = _dietGroups

    private val _groupName = mutableStateOf<List<String>>(emptyList())
    val groupName: State<List<String>> = _groupName

    private val _deletionSuccess = mutableStateOf(false)
    val deletionSuccess: State<Boolean> = _deletionSuccess

    private val _deletionError = mutableStateOf<String?>(null)
    val deletionError: State<String?> = _deletionError

    fun getGroups() {
        viewModelScope.launch {
            getGroupsUseCase().onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        println("Error: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data != null) {
                            _dietGroups.value = result.data
                            val groupNames = result.data.map { it.groupName }
                            _groupName.value = groupNames
                        } else {
                            println("null Error: ${result.message}")
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            deleteDietGroupUseCase.invoke(groupId).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        _deletionSuccess.value = false
                        _deletionError.value = result.message ?: "Connection problem."
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data == 200) {
                            _deletionSuccess.value = true
                            getGroups()
                        } else if (result.data==401) {
                            _deletionError.value = "You are not the owner of this group."
                        } else {
                            _deletionSuccess.value = false
                            _deletionError.value = "Connection problem."
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun resetDeletionSuccessState() {
        _deletionSuccess.value = false
        _deletionError.value = null
    }
}


