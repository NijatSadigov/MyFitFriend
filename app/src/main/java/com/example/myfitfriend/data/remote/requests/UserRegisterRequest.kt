package com.example.myfitfriend.data.remote.requests

data class UserRegisterRequest(
    val username: String,
    val passwordHash: String,
    val email: String,
    val weight:Double,
    val height:Double,
    val activityLevel:Int,
    val sex: Boolean,
    val age:Int,
    val lastEditDate:Long
)
