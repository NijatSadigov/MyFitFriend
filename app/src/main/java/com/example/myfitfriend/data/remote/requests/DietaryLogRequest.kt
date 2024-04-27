package com.example.myfitfriend.data.remote.requests

data class DietaryLogRequest(


    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val amountOfFood:Double,
    val foodId:Int
)
