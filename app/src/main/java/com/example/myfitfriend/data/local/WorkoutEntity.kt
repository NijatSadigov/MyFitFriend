package com.example.myfitfriend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable




@Entity(tableName = "WorkoutTable")
data class WorkoutEntity(
    val lastEditDate:Long,
    val description:String,
    val date:String,
    val userId:Int,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val isSync:Boolean=false,
    val title:String,
    val isAdded:Boolean =false,

    @PrimaryKey(autoGenerate = true)
    val workoutId: Int=0
)
