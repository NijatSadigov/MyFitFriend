package com.example.myfitfriend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable




@Entity(tableName = "DeletionTable")
data class DeletedItemsEntity(
    val typeOfItem:Int,//0dietaryLog, 1 workout 2 exercise
    val deletedId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int=0

)
