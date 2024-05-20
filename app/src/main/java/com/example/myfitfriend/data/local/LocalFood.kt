package com.example.myfitfriend.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
data class LocalFood(
    val foodId:Int,
    val foodName:String,
    val cal:Double,
    val protein:Double,
    val carb:Double,
    val fat:Double,
    val qrCode: String? = null  // Optional field, default is null





)
data class NetworkFood(
    val foodId:Int,
    val foodName:String,
    val cal:Double,
    val protein:Double,
    val carb:Double,
    val fat:Double,
    val qrCode: String? = null  // Optional field, default is null





)

@Entity(tableName = "LocalFoods")
data class LocalFoodEntity(

    val foodId:Int,
    val foodName:String,
    val cal:Double,
    val protein:Double,
    val carb:Double,
    val fat:Double,
    val qrCode: String? = null

)

