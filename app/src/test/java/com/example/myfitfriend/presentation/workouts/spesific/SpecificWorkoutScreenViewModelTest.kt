package com.example.myfitfriend.presentation.workouts.spesific
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.domain.use_case.exercise.DeleteExerciseByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.GetExerciseByWorkoutIdUseCaseLB
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository

import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
import com.example.myfitfriend.presentation.workouts.spesific.SpecificWorkoutScreenViewModel
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class SpecificWorkoutScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SpecificWorkoutScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val getWorkoutExercisesByIdUseCase = GetExerciseByWorkoutIdUseCaseLB(fakeRepository)
        val deleteExerciseByIdUseCase = DeleteExerciseByIdUseCaseLB(fakeRepository)
        val insertDeletionTableUseCaseLB = InsertDeletionTableUseCaseLB(fakeRepository)

        viewModel = SpecificWorkoutScreenViewModel(
            getWorkoutExercisesByIdUseCase,
            deleteExerciseByIdUseCase,
            insertDeletionTableUseCaseLB
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getExercises fetches and updates exercise list`() = runTest {
        // Mock data
        val workoutId = 1
        val exerciseList = listOf(
            ExerciseEntity(lastEditDate = System.currentTimeMillis(),workoutId= workoutId, title="Exercise 1", description = "Description 1", restTime = 90.0, repCount = 9, setCount = 4, weights = 12.0, exerciseId = 1),
            ExerciseEntity(lastEditDate = System.currentTimeMillis(),workoutId= workoutId, title="Exercise 2", description = "Description 2", restTime = 90.0, repCount = 9, setCount = 4, weights = 12.0, exerciseId = 2),
        )
        fakeRepository.setAllExercises(exerciseList)


        // Call the function
        viewModel.getExercises(workoutId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Exercises: ${viewModel.exercises.value}")

        // Assertions
        assert(viewModel.exercises.value.size == 2)
        assert(viewModel.exercises.value[0].exerciseId == 1)
        assert(viewModel.exercises.value[1].exerciseId == 2)
    }

    @Test
    fun `onDeleteExercise deletes exercise and updates UI`() = runTest {
        // Mock data
        val workoutId = 1
        val exerciseList = listOf(
            ExerciseEntity(lastEditDate = System.currentTimeMillis(),workoutId= workoutId, title="Exercise 1", description = "Description 1", restTime = 90.0, repCount = 9, setCount = 4, weights = 12.0, exerciseId = 1),
            ExerciseEntity(lastEditDate = System.currentTimeMillis(),workoutId= workoutId, title="Exercise 2", description = "Description 2", restTime = 90.0, repCount = 9, setCount = 4, weights = 12.0, exerciseId = 2),
        )
        fakeRepository.setAllExercises(exerciseList)

        // Mock the flow


        // Call the function
        viewModel.onDeleteExercise(1, workoutId, true)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Exercises after delete: ${viewModel.exercises.value}")
        println("Deletion Table: ${fakeRepository.getDeletionTable()}")

        // Assertions
        assert(viewModel.exercises.value.size==1)
        assert(fakeRepository.getDeletionTable().isNotEmpty())
        assert(fakeRepository.getDeletionTable().first().deletedId == 1)
    }
}
