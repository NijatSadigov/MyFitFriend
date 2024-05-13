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
import com.example.myfitfriend.domain.use_case.groups.mainscreen.GetGroupsUseCase
import com.example.myfitfriend.domain.use_case.groups.specific.GetGroupMembersByGroupIdUseCase
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
    private val getFoodUseCase:GetFoodUseCase,
    private val kickUserUseCase: KickUserUseCase,
    private val getGroupByIdUseCase: GetDietGroupByIdUseCase,
    private val getDietaryLogOdUserByUserIdForGroup: GetDietaryLogOdUserByUserIdForGroup,
    private val getUserDetailsByIdUseCase: GetUserDetailsByIdUseCase,
    private val getGroupMembersByGroupIdUseCase: GetGroupMembersByGroupIdUseCase
) : ViewModel(){

    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName

    private val _groupOwnerId= mutableStateOf(0)
    val groupOwnerId:State<Int> =_groupOwnerId

    private val _userId= mutableStateOf(0)
    val userId:State<Int> =_userId

    private val _groupOwnerName= mutableStateOf("")
    val groupOwnerName:State<String> =_groupOwnerName

    private val _membersId = mutableStateOf<List<Int>>(emptyList())
    val membersId: State<List<Int>> = _membersId

    private val _memberLogs = mutableStateOf<List<GroupDietaryLogsItem>>(emptyList())
    val memberLogs: State<List<GroupDietaryLogsItem>> =_memberLogs

    private val _successfullyKicked = mutableStateOf(false)
    val successfullyKicked: State<Boolean> =_successfullyKicked

    fun getGroupMembers(groupId:Int){
        viewModelScope.launch {

            getGroupMembersByGroupIdUseCase.invoke(groupId).onEach {
                result->
                when(result){
                    is Resources.Error ->    println("Error getGroupMembersByGroupIdUseCase: ${result.data} , ${result.message}")
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null) {
                            //println("getgroupMembers calls getUserID()")
                            //getUserId()
                            //println("after call ${userId.value}")
                            //println("getgroupMembers calls getGroup()")

                            //getGroup(groupId)
                            //println("after call ${groupName.value}")

                            //println("getgroupMembers calls getting members()")

                            //println("DEBUG SPECIFICGROUP : ${result.data}")
                            _membersId.value = result.data
                            println("getGroupMembersByGroupId why? ${membersId.value}")
                            getGroupMembersMaxValuesAndUserNames()
                           // println("userNames: ${userNames.value}, usermaxcals: ${userMaxCals.value}")
                        }
                    }
                }


            }.launchIn(viewModelScope)

            // getGroupMembersMaxValuesAndUserNames()

        }
    }

   fun getLogsOfMembers() {
        viewModelScope.launch {
            val memberLogs = mutableListOf<GroupDietaryLogsItem>()
                //println("getLogsOfMembers ${membersId.value}")
            membersId.value.forEach { wantedUserId ->
               // println("wantedUserId ${wantedUserId}")

                var totalCal = 0.0
                var totalProtein = 0.0
                var totalCarb = 0.0
                var totalFat = 0.0

                getDietaryLogOdUserByUserIdForGroup.invoke(wantedUserId).onEach { result ->
                    when (result) {
                        is Resources.Error -> {
                            println("Error getDietaryLogOdUserByUserIdForGroup: ${result.data} , ${result.message}")
                        }

                        is Resources.Loading -> {}
                        is Resources.Success ->
                        {
                          //  println("result ${result.data}")
                            result.data?.forEach { log ->
                                println("log $log")
                                getFoodUseCase(log.foodId).collect { foodResult ->
                                    when (foodResult) {
                                        is Resources.Error -> {}
                                        is Resources.Loading -> {}
                                        is Resources.Success -> {
                                            totalCal += (foodResult.data?.cal
                                                ?: 0.0) * log.amountOfFood / 100.0
                                            totalProtein += (foodResult.data?.protein
                                                ?: 0.0) * log.amountOfFood / 100.0
                                            totalCarb += (foodResult.data?.carb
                                                ?: 0.0) * log.amountOfFood / 100.0
                                            totalFat += (foodResult.data?.fat
                                                ?: 0.0) * log.amountOfFood / 100.0
                                            println(totalCal)
                                        }
                                    }
                                }
                            }
                    }
                    }

                }.launchIn(viewModelScope)
                // At the end of fetching all logs for this user
                val username = _memberUserNames.value[membersId.value.indexOf(wantedUserId)]
                val maxCalories = _memberUserMaxCals.value[membersId.value.indexOf(wantedUserId)]
                memberLogs.clear()
                val logItem = GroupDietaryLogsItem(username, wantedUserId, maxCalories, totalCal, totalProtein, totalCarb, totalFat)
                memberLogs.add(logItem)
                _memberLogs.value = memberLogs

            }
           // println("memberlogs in func: ${memberLogs}")
           // println("memberlogs in func: ${_memberLogs.value}")

        }
    }







    private val _memberUserNames = mutableStateOf<List<String>>(emptyList())
    val memberUserNames : State<List<String>> =_memberUserNames
    private val _memberUserMaxCals = mutableStateOf<List<Double>>(emptyList())
    val memberUserMaxCals :State <List<Double>> =_memberUserMaxCals

    fun getGroupMembersMaxValuesAndUserNames(){

        viewModelScope.launch {
            val userNames= mutableListOf<String>()
            val userMaxCals= mutableListOf<Double>()
          //  println("getGroupMembersMaxValuesAndUserNames first + ${membersId.value}")

            membersId.value.forEach {
                    id->
          //      println("getGroupMembersMaxValuesAndUserNames second + ${id}")
                getUserDetailsByIdUseCase.invoke(id).onEach { result ->
                    when (result) {
                        is Resources.Error -> {
                            println("Error getUserDetailsByIdUseCase: ${result.data} , ${result.message}")

                        }

                        is Resources.Loading -> {

                        }

                        is Resources.Success -> {
                  //          println("getGroupMembersMaxValuesAndUserNames third")
                            //_groupOwnerName.value = result.data!!.username
                            if(result.data!=null) {

                                val name = result.data.username
                                val totalCal=
                                    calculateDailyCalories(age=result.data.age, gender = result.data.sex, weight = result.data.weight, height = result.data.height, activityLevel = result.data.activityLevel)
                            //    println("name: $name, $totalCal")
                                userNames.add(name)
                                userMaxCals.add(totalCal)
                                _memberUserNames.value= userNames
                                _memberUserMaxCals.value=userMaxCals
                               // getLogsOfMembers()
                            //    println("userNames: ${userNames}, usermaxcals: ${userMaxCals}")
                            }
                        }
                    }
                }.launchIn(viewModelScope)

            }


            //  getLogsOfMembers()
        }



    }



    private fun calculateDailyCalories(age: Int, gender: Boolean, weight: Double, height: Double, activityLevel: Int): Double {
        val bmr: Double = if (gender) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        return when (activityLevel) {
            0 -> bmr * 1.2     // Sedentary
            1 -> bmr * 1.375   // Light exercise
            2 -> bmr * 1.55    // Moderate exercise
            3 -> bmr * 1.725   // High exercise
            else -> bmr        // Default is sedentary if invalid level
        }
    }
    fun getGroup(groupId: Int){
        viewModelScope.launch {
            getGroupByIdUseCase.invoke(groupId).onEach {
                    result->
                when(result){
                    is Resources.Error ->   {println("Error getGroup: ${result.data} , ${result.message}")}

                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(result.data!=null){
                            _groupName.value=result.data.groupName
                            _groupOwnerId.value=result.data.groupOwnerId
                            getOwnerUsername(groupOwnerId.value)
                          //  checkAreUOwner()

                        }



                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun kickUser(userId:Int,groupId: Int){
        viewModelScope.launch {
            kickUserUseCase.invoke(userId = userId,groupId=groupId).onEach {
                    result->
                when(result){
                    is Resources.Error -> {println("Error kickUser: ${result.data} , ${result.message}")}

                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (result.data==200){
                            _successfullyKicked.value=true
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun reverseSuccessfullyKicked(){
        _successfullyKicked.value=false

    }


    private fun getOwnerUsername(ownerId:Int){



        viewModelScope.launch {

            getUserDetailsByIdUseCase.invoke(ownerId).onEach {
                    result->
                when(result){
                    is Resources.Error -> {
                        println("Error getUserDetailsByIdUseCase: ${result.data} , ${result.message}")

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
    fun getUserId(){



        viewModelScope.launch {

            getUserCase.invoke().onEach {
                    result->
                when(result){
                    is Resources.Error -> {
                        println("Error getUserCase: ${result.data} , ${result.message}")

                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        if(result.data!=null) {
                            _userId.value = result.data.userId
                            println("user Id ${userId.value}")
                        }
                    }
                }
            }.launchIn(viewModelScope)



        }





    }
    private val _areUOwner = mutableStateOf(false)
    val areUOwner: State<Boolean> =_areUOwner
    fun checkAreUOwner(ownerId:Int, userId: Int){
       // println("ownerId:${groupOwnerId.value} : userId:${userId.value}")
        _areUOwner.value= ownerId==userId
    }
}