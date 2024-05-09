package com.example.myfitfriend.data.remote.reponses

data class DietaryLogResponse(
    val userId: Int,//
    val date: String,
    val partOfDay:Int, //0morning 1  lunch 2 dinner 3 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val dietaryLogId: Int//
)
