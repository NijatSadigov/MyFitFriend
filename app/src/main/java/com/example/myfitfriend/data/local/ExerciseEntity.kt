package com.example.myfitfriend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable




@Entity(tableName = "ExerciseTable")
data class ExerciseEntity(
    val lastEditDate:Long,
    val workoutId:Int,
    val description:String,
    val title:String,
    val weights:Double,
    val setCount:Int,
    val repCount:Int,
    val restTime:Double,
    val isSync:Boolean=false,
    val isAdded:Boolean=false,

    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int=0
)
