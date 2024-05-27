package com.example.myfitfriend.data.remote.reponses


data class User(
    val username: String,
    val passwordHash: String,
    val email: String,
    val weight:Double,
    val height:Double,
    val activityLevel:Int,
    val userId: Int,  // Assuming an auto-increment ID is used
    val sex: Boolean,
    val age:Int,
    val lastEditDate:Long,
    val isSync:Boolean
)
