package com.example.myfitfriend.data.local

import androidx.room.TypeConverter
import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.Workout
import okhttp3.internal.userAgent
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

//dao
//inject room
//local repository functions



fun FoodResponse.asLocalFood()=
    LocalFoodEntity(
        cal=cal,
        carb=carb,
        fat=fat,
        protein = protein,
        foodId = foodId.toInt(),
        foodName = foodName,
        qrCode=qrCode
    )






fun  DietaryLogEntity.asServerResponse()=
    DietaryLogResponse(
        amountOfFood = amountOfFood,
        date = date,
        foodId = foodId,
        dietaryLogId = dietaryLogId,
        foodItem = foodItem,
        userId = 0,
        partOfDay = partOfDay
    )

fun User.asUserEntity()=
    UserEntity(
        activityLevel = activityLevel,
        age = age,
        email = email,
        height = height,
        passwordHash = passwordHash,
        sex = sex,
        userId = userId,
        username = username,
        weight = weight,
        lastEditDate = lastEditDate,
        isSync = isSync
    )
fun UserEntity.asUser()=
    User(
        activityLevel = activityLevel,
        age = age,
        email = email,
        height = height,
        passwordHash = passwordHash,
        sex = sex,
        userId = userId,
        username = username,
        weight = weight,
        isSync = isSync,
        lastEditDate = lastEditDate
    )
fun LocalFoodEntity.asFoodResponse()=
    FoodResponse(
        cal=cal,
        carb=carb,
        fat=fat,
        foodName = foodName,
        foodId = foodId.toString(),
        qrCode = qrCode,
        protein = protein
    )
fun DietaryLogResponse.asDietaryLogEntity()=
    DietaryLogEntity(
        amountOfFood = amountOfFood,
        date = date,
        dietaryLogId = dietaryLogId,
        foodId = foodId,
        foodItem = foodItem,
        lastEditDate =  LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
,
        partOfDay = partOfDay,
        userId = userId
    )

fun Workout.asWorkoutEntity()=
    WorkoutEntity(
        date = date,
        description = description,
        userId = userId,
        lastEditDate = lastEditDate,
        title = title
    )
fun Exercise.asExerciseEntity()=
    ExerciseEntity(
        description = description,
        lastEditDate = lastEditDate,
        restTime = restTime,
        repCount = repCount,
        title = title,
        setCount = setCount,
        weights = weights,
        workoutId = workoutId
    )

