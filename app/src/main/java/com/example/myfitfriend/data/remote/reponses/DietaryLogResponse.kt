package com.example.myfitfriend.data.remote.reponses

data class DietaryLogResponse(
    val userId: Int,//
    val date: String,
    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val dietaryLogId: Int//
)
