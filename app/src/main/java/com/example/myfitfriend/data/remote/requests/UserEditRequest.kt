package com.example.myfitfriend.data.remote.requests

data class UserEditRequest(
    val username: String,
    val weight:Double,
    val height:Double,
    val activityLevel:Int
)