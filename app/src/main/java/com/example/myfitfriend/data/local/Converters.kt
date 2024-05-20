package com.example.myfitfriend.data.local

//dao
//inject room
//local repository functions



fun LocalDietaryLog.asEntity() = DietaryLogEntity(
   userId=userId,
    amountOfFood = amountOfFood,
    date = date,
    foodId = foodId,
    dietaryLogId = dietaryLogId,
    foodItem = foodItem,
    partOfDay = partOfDay
)

/**
 * Converts the local model to the external model for use
 * by layers external to the data layer
 */
fun DietaryLogEntity.asExternalModel() = LocalDietaryLog(
    userId=userId,
    amountOfFood = amountOfFood,
    date = date,
    foodId = foodId,
    dietaryLogId = dietaryLogId,
    foodItem = foodItem,
    partOfDay = partOfDay
)