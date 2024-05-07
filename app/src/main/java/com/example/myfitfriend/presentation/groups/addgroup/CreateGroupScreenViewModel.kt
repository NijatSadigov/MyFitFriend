package com.example.myfitfriend.presentation.groups.addgroup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.domain.use_case.groups.create.CreateDietGroupUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupScreenViewModel @Inject constructor(
private val createDietGroupUseCase: CreateDietGroupUseCase
) : ViewModel(){
    private val _groupName = mutableStateOf("")
    val groupName: State<String> =_groupName
    private val _successfulCreation = mutableStateOf(false)
    val successfulCreation: State<Boolean> =_successfulCreation
    fun onGroupNameChange(groupName:String){
        _groupName.value=groupName
    }

    fun onCreateGroup(){
        viewModelScope.launch {
            println("Launcs")
            createDietGroupUseCase(groupName.value).onEach { result->
                when(result){
                    is Resources.Error -> {
                        println("Error: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data==200)
                            _successfulCreation.value=true
                        else
                            println("null Error: ${result.message}")

                    }
                }

            }.launchIn(viewModelScope)



        }


    }




}