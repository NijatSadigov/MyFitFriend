package com.example.myfitfriend.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class LocalDietaryLog(
    val userId: Int,
    val date: String,
    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val dietaryLogId: Int = 0
)

data class NetworkDietaryLog(
    val userId: Int,
    val date: String,
    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val dietaryLogId: Int = 0
)

@Entity(tableName = "DietaryLogEntity")
data class DietaryLogEntity(

    val userId: Int,
    val date: String,
    val partOfDay:Int, //1morning 2 lunch 3 dinner 4 breakfast
    val foodItem: String,
    val foodId:Int,
    val amountOfFood:Double,
//    val calories: Int,
//    val carbs: Double,
//    val protein: Double,
//    val fats: Double,
    val dietaryLogId: Int
)
