package com.example.myfitfriend.presentation.dietarylogs.Foods

import androidx.annotation.Nullable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.data.local.asFoodResponse
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodsUseCaseLB
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.domain.use_case.dietarylogs.InsertDietaryLogCase
import com.example.myfitfriend.domain.use_case.dietarylogs.ShowFoodsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.UpdateDietaryLogUseCase
import com.example.myfitfriend.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowFoodsScreenViewModel @Inject constructor(
    private val showFoodsUseCase: GetFoodsUseCaseLB
):ViewModel() {
    private val _foods = mutableStateOf<List<FoodResponse>>(emptyList())
    val foods: State<List<FoodResponse>> =_foods

    private val _searchBar= mutableStateOf("")
    val searchBar:State<String> =_searchBar

    private val  _filteredFoods = mutableStateOf<List<FoodResponse>>(emptyList())
    val filteredFoods: State<List<FoodResponse>> =_filteredFoods


    fun onGetFoods(){
        viewModelScope.launch {
            showFoodsUseCase().onEach {
                result->
                when(result){
                    is Resources.Error -> {

                    }
                    is Resources.Loading -> {


                    }
                    is Resources.Success -> {
                       if(result.data!=null) {
                           _foods.value = result.data.map { it.asFoodResponse() }
                            _filteredFoods.value=foods.value
                       }
                    }
                }


            }.launchIn(viewModelScope)


        }
    }

    fun onSearchBarChange(text:String){
        _searchBar.value=text
        onFilterFoods()
    }
    private fun onFilterFoods() {
        val searchText = _searchBar.value.lowercase()  // Convert search text to lowercase for case-insensitive comparison

        // Filter foods list based on whether the food name contains the search text in lowercase
        _filteredFoods.value = _foods.value.filter { food ->
            // Convert food name to lowercase for case-insensitive comparison
            food.foodName.lowercase().contains(searchText)
        }
    }



}