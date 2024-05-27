package com.example.myfitfriend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Entity(tableName = "UserTable")
data class UserEntity(
    val username: String,
    val passwordHash: String,
    val email: String,
    val weight:Double,
    val height:Double,
    val activityLevel:Int,
    val age:Int,
    val sex:Boolean,
    var isSync:Boolean=true,
    var lastEditDate:Long,
    @PrimaryKey(autoGenerate = false)
    val userId:Int

)



