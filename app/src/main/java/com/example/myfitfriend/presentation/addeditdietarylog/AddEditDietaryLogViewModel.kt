package com.example.myfitfriend.presentation.addeditdietarylog

import androidx.annotation.Nullable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myfitfriend.domain.use_case.dietarylogs.InsertDietaryLogCase
import com.example.myfitfriend.domain.use_case.dietarylogs.UpdateDietaryLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditDietaryLogViewModel @Inject constructor(
    private val insertDietaryLogCase: InsertDietaryLogCase,
    private val updateDietaryLogUseCase: UpdateDietaryLogUseCase,

):ViewModel() {
    private val _newFoodId = mutableStateOf("")
    val newFoodId: State<String> =_newFoodId //int id

    private val _newPartOfDay = mutableStateOf("")
    val newPartOfDay: State<String> =_newPartOfDay //int 0-3

    private val _newFoodItem = mutableStateOf("")
    val newFoodItem: State<String> = _newFoodItem //string

    private val _newAmountOfFood = mutableStateOf("")
    val newAmountOfFood: State<String> =_newAmountOfFood //double

    private var dietaryLogId:Int = 0//null

    private val _navigateBack= mutableStateOf(false)
    val navigateBack:State<Boolean> = _navigateBack

    fun submitEditingNote(dietaryLogId:Int?,partOfDay:String?
    ,foodId:String?,amountOfFood:String?
    ){
        if(dietaryLogId!=null){
            _newPartOfDay.value=partOfDay ?:""
            _newFoodId.value= foodId?:""
            _newAmountOfFood.value=amountOfFood?:""
        }

    }





}