package com.example.myfitfriend.data.remote.reponses

data class Workout(
    val userId: Int,
    val description:String,
    val date: String, // Using String for simplicity, convert to date as needed
    val workoutId: Int = 0,
    val title:String
)