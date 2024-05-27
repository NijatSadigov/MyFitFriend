package com.example.myfitfriend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable




@Entity(tableName = "DietaryLogTable")
data class DietaryLogEntity(
    val lastEditDate:Long,
    val date: String,
    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
    val userId:Int,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    var isSync:Boolean=false,
    var isAdded:Boolean=false,
    @PrimaryKey(autoGenerate = true)
    val dietaryLogId: Int=0
)
