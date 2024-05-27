package com.MyFitFriend.data.model



data class Exercise(
    val workoutId:Int,
    val exerciseId: Int = 0,
    val title:String,
    val description: String="",
    val weights:Double=0.0,
    val setCount:Int=0,
    val repCount:Int=0,
    val restTime:Double=0.0,
    val lastEditDate:Long
)
