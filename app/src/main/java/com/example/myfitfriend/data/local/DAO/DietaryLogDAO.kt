package com.example.myfitfriend.data.local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity


@Dao
interface DietaryLogDAO {
    ////user


    @Upsert
    suspend fun addAndUpdateUser(user: UserEntity): Long

    @Query("SELECT * FROM UserTable")
    suspend fun getUser(): UserEntity?

    @Query("DELETE FROM UserTable")
    suspend fun clearAllUsers(): Int


    //dietaryLogs


    @Query("DELETE FROM DietaryLogTable")
    suspend fun clearDietaryLogs(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDietaryLogs(dietaryLogs: List<DietaryLogEntity>):List<Long>

    @Insert
    suspend fun addLogForToday(dietaryLogEntity: DietaryLogEntity): Long

    @Update
    suspend fun editLog(dietaryLogEntity: DietaryLogEntity): Int

    @Query("DELETE FROM DietaryLogTable WHERE dietaryLogId = :dietaryLogId")
    suspend fun removeLog(dietaryLogId: Int): Int

    @Query("SELECT * FROM DietaryLogTable WHERE date = :todayDate ORDER BY dietaryLogId ASC")
    suspend fun getTodayLogsByASCID(todayDate: String): List<DietaryLogEntity>


    @Query("SELECT * FROM DietaryLogTable WHERE dietaryLogId = :dietaryLogId LIMIT 1")
    suspend fun getDietaryLogById(
        dietaryLogId: Int
    ): DietaryLogEntity

    @Query("SELECT * FROM DietaryLogTable WHERE date = :date AND partOfDay = :partOfDay")
    suspend fun getDietaryLogByDateAndPartOfDay(
        date: String,
        partOfDay: Int
    ): List<DietaryLogEntity>

    @Query("SELECT * FROM DietaryLogTable")
    suspend fun getDietaryLogs(
    ): List<DietaryLogEntity>

    @Query("SELECT * FROM LocalFoodsTable WHERE qrCode = :qrCode")
    suspend fun getFoodByQRCode(qrCode:String):LocalFoodEntity



    //
    @Query("DELETE FROM LocalFoodsTable ")
    suspend fun clearFoods():Int

    @Query("SELECT * FROM LocalFoodsTable")
    suspend fun getAllFoods():List<LocalFoodEntity>

    @Query("Select * FROM localfoodstable WHERE foodId= :foodId LIMIT 1")
    suspend fun getFoodById(foodId:Int): LocalFoodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFoods(foods: List<LocalFoodEntity>):List<Long>


    //workout
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createWorkout(workoutEntity: WorkoutEntity):Long

    @Update
    suspend fun editWorkout(workoutEntity: WorkoutEntity):Int

    @Query(
        "DELETE from WorkoutTable where workoutId = :workoutId"
    )
    suspend fun deleteWorkout(workoutId: Int):Int

    @Query("Select * from WorkoutTable")
    suspend fun getWorkouts():List<WorkoutEntity>

    @Query("select * from WorkoutTable where workoutId = :workoutId")
    suspend fun getWorkoutById(workoutId:Int):WorkoutEntity

    @Query("DELETE FROM WorkoutTable")
    suspend fun clearAllWorkouts(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllWorkouts(workouts: List<WorkoutEntity>):List<Long>

    /////exercises

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createExercise(exerciseEntity: ExerciseEntity):Long

    @Update
    suspend fun editExercise(exerciseEntity: ExerciseEntity):Int

    @Query(
        "DELETE from ExerciseTable where exerciseId = :exerciseId"
    )
    suspend fun deleteExercise(exerciseId: Int):Int

    @Query("Select * from ExerciseTable")
    suspend fun getExercises():List<ExerciseEntity>

    @Query("select * from ExerciseTable where exerciseId = :exerciseId")
    suspend fun getExerciseByExerciseId(exerciseId:Int):ExerciseEntity

    @Query("select * from ExerciseTable where workoutId = :workoutId")
    suspend fun getExercisesByWorkoutId(workoutId:Int):List<ExerciseEntity>

    @Query("DELETE FROM ExerciseTable")
    suspend fun clearAllExercises(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllExercises(exercises: List<ExerciseEntity>):List<Long>

    @Query("DELETE FROM ExerciseTable where workoutId= :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Int)

    @Transaction
    suspend fun deleteWorkoutAndExercises(workoutId: Int): Int {
        deleteExercisesByWorkoutId(workoutId)
        return deleteWorkout(workoutId)
    }

    ////deletionSYNC
    @Query("Select * from DeletionTable")
    suspend fun getDeletionTable():List<DeletedItemsEntity>
    @Query("Delete from DeletionTable")
    suspend fun clearDeletionTable() :Int
    @Insert
    suspend fun insertDeletionEntity(deletedItemsEntity: DeletedItemsEntity):Long


}