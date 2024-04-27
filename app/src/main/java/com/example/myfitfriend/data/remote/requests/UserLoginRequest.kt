package com.example.myfitfriend.data.remote.requests

data class UserLoginRequest(
    val email:String,
    val passwordHash:String
)
