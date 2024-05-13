package com.example.myfitfriend.data.remote.reponses

data class UserResponse(
    val username: String,
//    val email: String,
    val weight:Double,
    val height:Double,
    val activityLevel:Int,
    val sex: Boolean,
    val age:Int
)
