package com.example.myfitfriend.presentation.groups.editgroup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.domain.use_case.groups.create.CreateDietGroupUseCase
import com.example.myfitfriend.domain.use_case.groups.edit.EditDietGroupUseCase
import com.example.myfitfriend.domain.use_case.groups.edit.GetDietGroupByIdUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupScreenViewModel @Inject constructor(
    private val editDietGroupUseCase: EditDietGroupUseCase,
    private val getDietGroupByIdUseCase: GetDietGroupByIdUseCase
) : ViewModel() {
    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName
    private val _successfulEdition = mutableStateOf(false)
    val successfulEdition: State<Boolean> = _successfulEdition
    private val _message = mutableStateOf("")
    val message: State<String> = _message

    fun onGroupNameChange(groupName: String) {
        _groupName.value = groupName
    }

    fun onEditGroup(groupId: Int) {
        viewModelScope.launch {
            editDietGroupUseCase.invoke(groupId = groupId, groupName = groupName.value).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        _message.value = result.message ?: "Error editing group"
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data == 200) {
                            _successfulEdition.value = true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getDietGroup(groupId: Int) {
        viewModelScope.launch {
            getDietGroupByIdUseCase.invoke(groupId).onEach { result ->
                when (result) {
                    is Resources.Error -> {
                        _message.value = result.message ?: "Failed to load group"
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data != null) {
                            _groupName.value = result.data.groupName
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
