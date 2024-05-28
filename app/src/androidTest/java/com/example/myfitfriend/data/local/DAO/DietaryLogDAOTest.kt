package com.example.myfitfriend.data.local.DAO

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.db.LocalDB
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
 class DietaryLogDAOTest {

    private lateinit var database: LocalDB
    private lateinit var dao:DietaryLogDAO

    @Before
    fun setup(){
        database=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDB::class.java

        ).allowMainThreadQueries().build()
        dao=database.dietaryLogDao()
    }
    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertDietaryLog()= runTest{
        val dietaryLogId=1
        val dietaryLog =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1,
            dietaryLogId = dietaryLogId
            )
        dao.addLogForToday(dietaryLog)

        val insertedLog=dao.getDietaryLogById(dietaryLogId)
        assertThat(insertedLog).isNotNull()
        assertThat(insertedLog.dietaryLogId).isEqualTo(dietaryLogId)
        assertThat(insertedLog.amountOfFood).isEqualTo(100.0)

    }
    @Test
    fun editDietaryLog()= runTest{
        val dietaryLogId=1
        val dietaryLog =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1,
            dietaryLogId = dietaryLogId
        )
        dao.addLogForToday(dietaryLog)


        val dietaryLog2 =DietaryLogEntity(amountOfFood = 200.0,
            date = LocalDate.now().toString(),
            foodId = 2,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 1,
            userId = 1,
            dietaryLogId = dietaryLogId
        )
        dao.editLog(dietaryLog2)

        val editedLog=dao.getDietaryLogById(dietaryLogId)
        assertThat(editedLog.amountOfFood).isEqualTo(200.0)
        assertThat(editedLog.partOfDay).isEqualTo(1)
        assertThat(editedLog.foodId).isEqualTo(2)


    }

@Test
    fun deleteDietaryLog()= runTest{
        val dietaryLogId=1
        val dietaryLog =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1,
            dietaryLogId = dietaryLogId
        )
        dao.addLogForToday(dietaryLog)

        dao.removeLog(dietaryLogId)
        val insertedLog=dao.getDietaryLogById(dietaryLogId)
        assertThat(insertedLog).isNull()


    }
    @Test
    fun insertDietaryLogList()= runTest{
        val dietaryLog1 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1
        )
        val dietaryLog2 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 2,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 1,
            userId = 1
        )
        val dietaryLog3 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 3,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 2,
            userId = 1
        )
        val dietaryLog4 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 4,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 3,
            userId = 1
        )
        val insertionLogs= listOf(dietaryLog1,dietaryLog2,dietaryLog3,dietaryLog4)
       dao.insertAllDietaryLogs(insertionLogs)
        val logsInDB=dao.getDietaryLogs()
        assertThat(logsInDB).isNotNull()

        assertThat(logsInDB.size).isEqualTo(4)

    }

    @Test
    fun clearDietaryAllLogs()= runTest{
        val dietaryLog1 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1
        )
        val dietaryLog2 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 2,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 1,
            userId = 1
        )
        val dietaryLog3 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 3,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 2,
            userId = 1
        )
        val dietaryLog4 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 4,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 3,
            userId = 1
        )
        val insertionLogs= listOf(dietaryLog1,dietaryLog2,dietaryLog3,dietaryLog4)
        dao.insertAllDietaryLogs(insertionLogs)

        //clear db
        dao.clearDietaryLogs()
        val logsInDB=dao.getDietaryLogs()
        println(logsInDB.size)

        assertThat(logsInDB.size).isEqualTo(0)

    }
@Test
    fun getTodayDietaryLogs()= runTest{
        val dietaryLog1 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1
        )
        val dietaryLog2 =DietaryLogEntity(amountOfFood = 100.0,
            date ="2024-04-26",
            foodId = 2,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 1,
            userId = 1
        )
        val dietaryLog3 =DietaryLogEntity(amountOfFood = 100.0,
            date = "2024-05-26",
            foodId = 3,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 2,
            userId = 1
        )
        val dietaryLog4 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 4,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 3,
            userId = 1
        )
        val insertionLogs= listOf(dietaryLog1,dietaryLog2,dietaryLog3,dietaryLog4)

        dao.insertAllDietaryLogs(insertionLogs)

    val rightLogs= dao.getDietaryLogs().filter { it.date==LocalDate.now().toString() }

    val getLogs=dao.getTodayLogsByASCID(LocalDate.now().toString())
    println(getLogs)

    assertThat(getLogs).isEqualTo(rightLogs)


    }
    @Test
    fun getDietaryLogByDateAndPartOfDay()= runTest{
        val dietaryLog1 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 1,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 0,
            userId = 1
        )
        val dietaryLog2 =DietaryLogEntity(amountOfFood = 100.0,
            date ="2024-04-26",
            foodId = 2,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 1,
            userId = 1
        )
        val dietaryLog3 =DietaryLogEntity(amountOfFood = 100.0,
            date = "2024-05-26",
            foodId = 3,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 2,
            userId = 1
        )
        val dietaryLog4 =DietaryLogEntity(amountOfFood = 100.0,
            date = LocalDate.now().toString(),
            foodId = 4,
            foodItem = "",
            lastEditDate = System.currentTimeMillis(),
            partOfDay = 3,
            userId = 1
        )
        val insertionLogs= listOf(dietaryLog1,dietaryLog2,dietaryLog3,dietaryLog4)
        dao.insertAllDietaryLogs(insertionLogs)
        val rightLogs= dao.getDietaryLogs().filter { it.date==LocalDate.now().toString() && it.partOfDay==3 }

        val getLogs=dao.getDietaryLogByDateAndPartOfDay(LocalDate.now().toString(),3)
        assertThat(getLogs).isEqualTo(rightLogs)


    }

    //user
    @Test
    fun addAndUpdateUser()= runTest{
        val user= UserEntity(
            activityLevel = 1,
            age = 12,
            email = "sample@mail.com",
            height = 181.2,
            lastEditDate = System.currentTimeMillis(),
            passwordHash = "passwordHash",
            sex=true,
            userId = 1,
            username = "sample example",
            weight = 80.7
        )
        dao.addAndUpdateUser(user)

        val userInDb=dao.getUser()

        assertThat(userInDb).isEqualTo(user)
        val user2= UserEntity(
            activityLevel = 1,
            age = 12,
            email = "sample@mail.com",
            height = 151.2,
            lastEditDate = System.currentTimeMillis(),
            passwordHash = "passwordHash",
            sex=false,
            userId = 1,
            username = "sample example",
            weight = 80.7
        )
        dao.addAndUpdateUser(user2)

        val userInDb2=dao.getUser()

        assertThat(userInDb2).isEqualTo(user2)


    }

    @Test
    fun clearAllUsers()= runTest{
        val user= UserEntity(
            activityLevel = 1,
            age = 12,
            email = "sample@mail.com",
            height = 181.2,
            lastEditDate = System.currentTimeMillis(),
            passwordHash = "passwordHash",
            sex=true,
            userId = 1,
            username = "sample example",
            weight = 80.7
        )
        dao.addAndUpdateUser(user)

        val userInDb=dao.getUser()

        assertThat(userInDb).isEqualTo(user)
        val user2= UserEntity(
            activityLevel = 1,
            age = 12,
            email = "sample@mail.com",
            height = 151.2,
            lastEditDate = System.currentTimeMillis(),
            passwordHash = "passwordHash",
            sex=false,
            userId = 2,
            username = "sample example",
            weight = 80.7
        )
        dao.clearAllUsers()
        assertThat(dao.getUser()).isNull()
    }


    //foods
    @Test
    fun insertAllFoods()= runTest {
        val foods = listOf(
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 1,
                foodName = "sample1",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 2,
                foodName = "sample2",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 3,
                foodName = "sample3",
                protein = 25.0


            )
        )

        dao.insertAllFoods(foods =foods)
        val foodsFromDb=dao.getAllFoods()
        assertThat(foodsFromDb).isEqualTo(foods)


    }

    @Test
    fun clearAllFoods()= runTest {
        val foods = listOf(
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 1,
                foodName = "sample1",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 2,
                foodName = "sample2",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 3,
                foodName = "sample3",
                protein = 25.0


            )
        )

        dao.insertAllFoods(foods =foods)
        dao.clearFoods()
        val foodsFromDb=dao.getAllFoods()
        assertThat(foodsFromDb.size).isEqualTo(0)



    }
    @Test
    fun getFoodByQRCode()= runTest {
        val foods = listOf(
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 1,
                foodName = "sample1",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 2,
                foodName = "sample2",
                protein = 25.0,
                qrCode = "12345678912345"


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 3,
                foodName = "sample3",
                protein = 25.0


            )
        )

        dao.insertAllFoods(foods =foods)
        val foodFromDb=dao.getFoodByQRCode("12345678912345")
        assertThat(foodFromDb.foodName).isEqualTo("sample2")



    }

    @Test
    fun getFoodById()= runTest {
        val foods = listOf(
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 1,
                foodName = "sample1",
                protein = 25.0


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 2,
                foodName = "sample2",
                protein = 25.0,
                qrCode = "12345678912345"


            ),
            LocalFoodEntity(
                cal=300.0,
                carb=25.2,
                fat=11.3,
                foodId = 3,
                foodName = "sample3",
                protein = 25.0


            )
        )

        dao.insertAllFoods(foods =foods)
        val foodFromDb=dao.getFoodById(1)
        assertThat(foodFromDb!!.foodName).isEqualTo("sample1")



    }
    //workouts
    @Test
    fun createWorkout() = runTest {
        val workout = WorkoutEntity(
            lastEditDate = System.currentTimeMillis(),
            description = "Sample Workout",
            date = LocalDate.now().toString(),
            userId = 1,
            isSync = false,
            title = "Morning Workout",
            isAdded = false
        )
        val workoutId = dao.createWorkout(workout)
        val workoutFromDb = dao.getWorkoutById(workoutId.toInt())
        assertThat(workoutFromDb).isNotNull()
        assertThat(workoutFromDb.workoutId).isEqualTo(workoutId.toInt())
        assertThat(workoutFromDb.title).isEqualTo("Morning Workout")
    }

    @Test
    fun editWorkout() = runTest {
        val workout = WorkoutEntity(
            lastEditDate = System.currentTimeMillis(),
            description = "Sample Workout",
            date = LocalDate.now().toString(),
            userId = 1,
            isSync = false,
            title = "Morning Workout",
            isAdded = false
        )
        val workoutId = dao.createWorkout(workout)
        val updatedWorkout = workout.copy(workoutId = workoutId.toInt(), title = "Evening Workout")
        dao.editWorkout(updatedWorkout)
        val workoutFromDb = dao.getWorkoutById(workoutId.toInt())
        assertThat(workoutFromDb.title).isEqualTo("Evening Workout")
    }

    @Test
    fun deleteWorkout() = runTest {
        val workout = WorkoutEntity(
            lastEditDate = System.currentTimeMillis(),
            description = "Sample Workout",
            date = LocalDate.now().toString(),
            userId = 1,
            isSync = false,
            title = "Morning Workout",
            isAdded = false
        )
        val workoutId = dao.createWorkout(workout)
        dao.deleteWorkout(workoutId.toInt())
        val workoutFromDb = dao.getWorkoutById(workoutId.toInt())
        assertThat(workoutFromDb).isNull()
    }

    @Test
    fun setAllWorkouts() = runTest {
        val workouts = listOf(
            WorkoutEntity(
                lastEditDate = System.currentTimeMillis(),
                description = "Workout 1",
                date = LocalDate.now().toString(),
                userId = 1,
                isSync = false,
                title = "Workout 1",
                isAdded = false
            ),
            WorkoutEntity(
                lastEditDate = System.currentTimeMillis(),
                description = "Workout 2",
                date = LocalDate.now().toString(),
                userId = 1,
                isSync = false,
                title = "Workout 2",
                isAdded = false
            )
        )
        dao.setAllWorkouts(workouts)
        val workoutsFromDb = dao.getWorkouts()
        assertThat(workoutsFromDb.size).isEqualTo(workouts.size)
    }

    @Test
    fun clearAllWorkouts() = runTest {
        val workouts = listOf(
            WorkoutEntity(
                lastEditDate = System.currentTimeMillis(),
                description = "Workout 1",
                date = LocalDate.now().toString(),
                userId = 1,
                isSync = false,
                title = "Workout 1",
                isAdded = false
            ),
            WorkoutEntity(
                lastEditDate = System.currentTimeMillis(),
                description = "Workout 2",
                date = LocalDate.now().toString(),
                userId = 1,
                isSync = false,
                title = "Workout 2",
                isAdded = false
            )
        )
        dao.setAllWorkouts(workouts)
        dao.clearAllWorkouts()
        val workoutsFromDb = dao.getWorkouts()
        assertThat(workoutsFromDb.size).isEqualTo(0)
    }

   ///exercise
    @Test
    fun createExercise() = runTest {
        val exercise = ExerciseEntity(
            lastEditDate = System.currentTimeMillis(),
            workoutId = 1,
            description = "Sample Exercise",
            title = "Bicep Curl",
            weights = 20.0,
            setCount = 3,
            repCount = 12,
            restTime = 60.0,
            isSync = false,
            isAdded = false
        )
        val exerciseId = dao.createExercise(exercise)
        val exerciseFromDb = dao.getExerciseByExerciseId(exerciseId.toInt())
        assertThat(exerciseFromDb).isNotNull()
        assertThat(exerciseFromDb.exerciseId).isEqualTo(exerciseId.toInt())
        assertThat(exerciseFromDb.title).isEqualTo("Bicep Curl")
    }

    @Test
    fun editExercise() = runTest {
        val exercise = ExerciseEntity(
            lastEditDate = System.currentTimeMillis(),
            workoutId = 1,
            description = "Sample Exercise",
            title = "Bicep Curl",
            weights = 20.0,
            setCount = 3,
            repCount = 12,
            restTime = 60.0,
            isSync = false,
            isAdded = false
        )
        val exerciseId = dao.createExercise(exercise)
        val updatedExercise = exercise.copy(exerciseId = exerciseId.toInt(), title = "Tricep Extension")
        dao.editExercise(updatedExercise)
        val exerciseFromDb = dao.getExerciseByExerciseId(exerciseId.toInt())
        assertThat(exerciseFromDb.title).isEqualTo("Tricep Extension")
    }

    @Test
    fun deleteExercise() = runTest {
        val exercise = ExerciseEntity(
            lastEditDate = System.currentTimeMillis(),
            workoutId = 1,
            description = "Sample Exercise",
            title = "Bicep Curl",
            weights = 20.0,
            setCount = 3,
            repCount = 12,
            restTime = 60.0,
            isSync = false,
            isAdded = false
        )
        val exerciseId = dao.createExercise(exercise)
        dao.deleteExercise(exerciseId.toInt())
        val exerciseFromDb = dao.getExerciseByExerciseId(exerciseId.toInt())
        assertThat(exerciseFromDb).isNull()
    }

    @Test
    fun setAllExercises() = runTest {
        val exercises = listOf(
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 1",
                title = "Exercise 1",
                weights = 15.0,
                setCount = 3,
                repCount = 10,
                restTime = 60.0,
                isSync = false,
                isAdded = false
            ),
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 2",
                title = "Exercise 2",
                weights = 20.0,
                setCount = 4,
                repCount = 12,
                restTime = 90.0,
                isSync = false,
                isAdded = false
            )
        )
        dao.setAllExercises(exercises)
        val exercisesFromDb = dao.getExercises()
        assertThat(exercisesFromDb.size).isEqualTo(exercises.size)
    }

    @Test
    fun clearAllExercises() = runTest {
        val exercises = listOf(
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 1",
                title = "Exercise 1",
                weights = 15.0,
                setCount = 3,
                repCount = 10,
                restTime = 60.0,
                isSync = false,
                isAdded = false
            ),
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 2",
                title = "Exercise 2",
                weights = 20.0,
                setCount = 4,
                repCount = 12,
                restTime = 90.0,
                isSync = false,
                isAdded = false
            )
        )
        dao.setAllExercises(exercises)
        dao.clearAllExercises()
        val exercisesFromDb = dao.getExercises()
        assertThat(exercisesFromDb.size).isEqualTo(0)
    }

    @Test
    fun getExercisesByWorkoutId() = runTest {
        val exercises = listOf(
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 1",
                title = "Exercise 1",
                weights = 15.0,
                setCount = 3,
                repCount = 10,
                restTime = 60.0,
                isSync = false,
                isAdded = false
            ),
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 1,
                description = "Exercise 2",
                title = "Exercise 2",
                weights = 20.0,
                setCount = 4,
                repCount = 12,
                restTime = 90.0,
                isSync = false,
                isAdded = false
            ),
            ExerciseEntity(
                lastEditDate = System.currentTimeMillis(),
                workoutId = 2,
                description = "Exercise 3",
                title = "Exercise 3",
                weights = 25.0,
                setCount = 5,
                repCount = 15,
                restTime = 120.0,
                isSync = false,
                isAdded = false
            )
        )
        dao.setAllExercises(exercises)
        val exercisesForWorkout1 = dao.getExercisesByWorkoutId(1)
        assertThat(exercisesForWorkout1.size).isEqualTo(2)
        assertThat(exercisesForWorkout1.all { it.workoutId == 1 }).isTrue()
    }

    //deleteion table
    @Test
    fun insertDeletionEntity() = runTest {
        val deletedItem = DeletedItemsEntity(
            typeOfItem = 1,
            deletedId = 1
        )
        val deletionId = dao.insertDeletionEntity(deletedItem)
        val deletionFromDb = dao.getDeletionTable().first { it.id == deletionId.toInt() }
        assertThat(deletionFromDb).isNotNull()
        assertThat(deletionFromDb.typeOfItem).isEqualTo(1)
        assertThat(deletionFromDb.deletedId).isEqualTo(1)
    }

    @Test
    fun getDeletionTable() = runTest {
        val deletedItems = listOf(
            DeletedItemsEntity(
                typeOfItem = 0,
                deletedId = 1
            ),
            DeletedItemsEntity(
                typeOfItem = 1,
                deletedId = 2
            ),
            DeletedItemsEntity(
                typeOfItem = 2,
                deletedId = 3
            )
        )
        deletedItems.forEach { dao.insertDeletionEntity(it) }
        val deletionsFromDb = dao.getDeletionTable()
        assertThat(deletionsFromDb.size).isEqualTo(deletedItems.size)
    }

    @Test
    fun clearDeletionTable() = runTest {
        val deletedItems = listOf(
            DeletedItemsEntity(
                typeOfItem = 0,
                deletedId = 1
            ),
            DeletedItemsEntity(
                typeOfItem = 1,
                deletedId = 2
            ),
            DeletedItemsEntity(
                typeOfItem = 2,
                deletedId = 3
            )
        )
        deletedItems.forEach { dao.insertDeletionEntity(it) }
        dao.clearDeletionTable()
        val deletionsFromDb = dao.getDeletionTable()
        assertThat(deletionsFromDb.size).isEqualTo(0)
    }




}