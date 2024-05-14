package com.example.myfitfriend.presentation.groups.invites


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.reponses.AddFriendRequestInfo
import com.example.myfitfriend.domain.use_case.groups.invite.AnswerInviteUseCase
import com.example.myfitfriend.domain.use_case.groups.invite.GetRequestsUseCase
import com.example.myfitfriend.util.Resources

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class InvitesScreenViewModel @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase,
    private val answerInviteUseCase: AnswerInviteUseCase
 ) : ViewModel(){
    private val _invites = mutableStateOf<List<AddFriendRequestInfo>>(emptyList())
    val invites: State<List<AddFriendRequestInfo>> =_invites

    fun getInvites(){
        viewModelScope.launch {
            getRequestsUseCase.invoke().onEach {
                    r->

                when(r){
                    is Resources.Error -> {
                        println("error in getting invites: ${r.message}")}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(r.data!=null){
                            _invites.value=r.data
                        }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
    fun answerInvite(answer:Boolean,requestId:Int){
        viewModelScope.launch {
            answerInviteUseCase.invoke(answer=answer, requestId = requestId).onEach {
                    r->

                when(r){
                    is Resources.Error -> {
                        println("error in getting invites: ${r.message}")}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        println("answer: $answer")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }



 }