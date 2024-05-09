package com.example.myfitfriend.data.remote.reponses

data class DietaryLogFrameItem(
    val foodName:String,
    val foodId:String,
    val cal:Double,
    val carb:Double,
    val protein:Double,
    val fat:Double,
    val qrCode:String?,
    val dietaryLogId: Int//
)
