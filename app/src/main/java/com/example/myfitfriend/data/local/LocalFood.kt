package com.example.myfitfriend.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Entity(tableName = "LocalFoodsTable")
data class LocalFoodEntity(
    @PrimaryKey(autoGenerate = false)
    val foodId:Int,
    val foodName:String,
    val cal:Double,
    val protein:Double,
    val carb:Double,
    val fat:Double,
    val qrCode: String? = null

)

