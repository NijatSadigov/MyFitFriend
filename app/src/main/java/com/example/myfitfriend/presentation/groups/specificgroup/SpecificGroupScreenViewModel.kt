package com.example.myfitfriend.presentation.groups.specificgroup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.remote.reponses.GroupDietaryLogsItem
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogByIdUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogOdUserByUserIdForGroup
import com.example.myfitfriend.domain.use_case.dietarylogs.GetFoodUseCase
import com.example.myfitfriend.domain.use_case.groups.delete.KickUserUseCase
import com.example.myfitfriend.domain.use_case.groups.edit.GetDietGroupByIdUseCase
import com.example.myfitfriend.domain.use_case.groups.invite.InviteUserUseCase
import com.example.myfitfriend.domain.use_case.groups.mainscreen.GetGroupsUseCase
import com.example.myfitfriend.domain.use_case.groups.specific.GetGroupLogsUseCase
import com.example.myfitfriend.domain.use_case.users.GetUserDetailsByIdUseCase
import com.example.myfitfriend.domain.use_case.users.ProfileUserCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SpecificGroupScreenViewModel @Inject constructor(
    private val getUserCase: ProfileUserCase,
    private val getDietGroupByIdUseCase: GetDietGroupByIdUseCase,
    private val kickUserUseCase: KickUserUseCase,
    private val inviteUserUseCase: InviteUserUseCase,
    private val getGroupLogsUseCase: GetGroupLogsUseCase,
    private val getUserDetailsByIdUseCase: GetUserDetailsByIdUseCase

) : ViewModel(){

    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName

    private val _groupOwnerId= mutableStateOf(0)
    val groupOwnerId:State<Int> =_groupOwnerId

    private val _userId= mutableStateOf(0)
    val userId:State<Int> =_userId

    private val _groupOwnerName= mutableStateOf("")
    val groupOwnerName:State<String> =_groupOwnerName

    private val _memberLogs = mutableStateOf<List<GroupDietaryLogsItem>>(emptyList())
    val memberLogs: State<List<GroupDietaryLogsItem>> =_memberLogs

    private val _successfullyKicked = mutableStateOf(false)
    val successfullyKicked: State<Boolean> =_successfullyKicked

    private val _showToast = mutableStateOf<String?>(null)
    val showToast: State<String?> = _showToast

    fun getGroupMembersLogs(groupId:Int){
        viewModelScope.launch {
            getGroupLogsUseCase.invoke(groupId).onEach { r ->
                when(r){
                    is Resources.Error -> {
                        println("error in _memberLogs")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        println("getGroup ${r.data}")
                        if(r.data!=null){
                            _groupOwnerName.value = r.data.find { it.userId == groupOwnerId.value }!!.userName
                            _memberLogs.value = r.data
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getOwnerName(){
        viewModelScope.launch {
            getUserDetailsByIdUseCase.invoke(groupOwnerId.value).onEach { r ->
                when(r){
                    is Resources.Error -> {
                        println("err at getOwnerName :${r.message} : ${r.data}")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        _groupOwnerName.value = r.data?.username ?: ""
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getGroup(groupId: Int){
        viewModelScope.launch {
            getDietGroupByIdUseCase.invoke(groupId).onEach { r ->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(r.data!=null){
                            println("getGroup")
                            _groupName.value = r.data.groupName
                            _groupOwnerId.value = r.data.groupOwnerId
                            getUserId()
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun kickUser(userId: Int, groupId: Int){
        viewModelScope.launch {
            kickUserUseCase.invoke(userId = userId, groupId = groupId).onEach { result ->
                when(result){
                    is Resources.Error -> {
                        println("Error kickUser: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data == 200){
                            _successfullyKicked.value = true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun reverseSuccessfullyKicked(){
        _successfullyKicked.value = false
    }

    fun inviteMemberByEmail(wantedUserEmail: String, groupId: Int){
        viewModelScope.launch {
            inviteUserUseCase.invoke(wantedUserEmail = wantedUserEmail, groupId = groupId).onEach { result ->
                when(result){
                    is Resources.Error -> {
                        println("error at invite: ${result.data} : ${result.message}")

                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data ==406) {
                            _showToast.value = "User is already invited."
                        }
                        else if(result.data == 200){
                            _showToast.value = "User Invited"
                        } else if (result.data == 409) {
                            _showToast.value = "User is already in the group."
                        }
                        else if(result.data == 404)
                            _showToast.value = "User Not FOund"

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getUserId(){
        viewModelScope.launch {
            getUserCase.invoke().onEach { result ->
                when(result){
                    is Resources.Error -> {
                        println("Error getUserCase: ${result.data} , ${result.message}")
                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data != null) {
                            _userId.value = result.data.userId
                            println("user Id ${userId.value}")
                            checkAreUOwner(ownerId = groupOwnerId.value, userId = userId.value)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _areUOwner = mutableStateOf(false)
    val areUOwner: State<Boolean> = _areUOwner

    fun checkAreUOwner(ownerId: Int, userId: Int){
        println("ownerId:$ownerId : userId:$userId")
        _areUOwner.value = ownerId == userId
    }

    fun clearToast() {
        _showToast.value = null
    }
}