package com.example.myfitfriend.presentation.workouts.exercises.editexercise
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.domain.use_case.exercise.GetExerciseByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.UpdateExerciseUseCaseLB
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository
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
class EditExerciseScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditExerciseScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val updateExerciseUseCase = UpdateExerciseUseCaseLB(fakeRepository)
        val getExerciseUseCase = GetExerciseByIdUseCaseLB(fakeRepository)

        viewModel = EditExerciseScreenViewModel(updateExerciseUseCase, getExerciseUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentDetails fetches and updates exercise details`() = runTest {
        // Mock data
        val exerciseId = 1
        val exerciseEntity = ExerciseEntity(
            exerciseId = exerciseId,
            workoutId = 1,
            title = "Push Ups",
            description = "Do 3 sets of push ups",
            weights = 0.0,
            setCount = 3,
            repCount = 15,
            restTime = 60.0,
            lastEditDate = System.currentTimeMillis()
        )
        fakeRepository.setAllExercises(listOf(exerciseEntity))


        // Call the function
        viewModel.getCurrentDetails(exerciseId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Title: ${viewModel.title.value}")
        println("Description: ${viewModel.description.value}")
        println("Weights: ${viewModel.weights.value}")
        println("SetCount: ${viewModel.setCount.value}")
        println("RepCount: ${viewModel.repCount.value}")
        println("RestTime: ${viewModel.restTime.value}")

        // Assertions
        assert(viewModel.title.value == "Push Ups")
        assert(viewModel.description.value == "Do 3 sets of push ups")
        assert(viewModel.weights.value == "0.0")
        assert(viewModel.setCount.value == "3")
        assert(viewModel.repCount.value == "15")
        assert(viewModel.restTime.value == "60.0")
    }

    @Test
    fun `onSubmitEditedExercise updates exercise successfully`() = runTest {
        // Mock data
        val workoutId = 1
        val exerciseId = 1
        val exerciseEntity = ExerciseEntity(
            exerciseId = exerciseId,
            workoutId = workoutId,
            title = "Push Ups",
            description = "Do 3 sets of push ups",
            weights = 0.0,
            setCount = 3,
            repCount = 15,
            restTime = 60.0,
            lastEditDate = System.currentTimeMillis()
        )
        fakeRepository.setAllExercises(listOf(exerciseEntity))

        // Mock the flow

        // Set ViewModel state
        viewModel.onTitleChanged("Push Ups")
        viewModel.onDescriptionChanged("Do 3 sets of push ups")
        viewModel.onWeightsChanged("0.0")
        viewModel.onSetCountChanged("3")
        viewModel.onRepCountChanged("15")
        viewModel.onRestTimeChanged("60.0")

        // Call the function
        viewModel.onSubmitEditedExercise(workoutId, exerciseId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("OnSuccessfullyEdit: ${viewModel.onSuccessfullyEdit.value}")

        // Assertions
        assert(viewModel.onSuccessfullyEdit.value)
    }

    @Test
    fun `onTitleChanged updates title state`() {
        // Set title
        val title = "New Exercise"
        viewModel.onTitleChanged(title)

        // Assertions
        assert(viewModel.title.value == title)
    }

    @Test
    fun `onDescriptionChanged updates description state`() {
        // Set description
        val description = "Exercise Description"
        viewModel.onDescriptionChanged(description)

        // Assertions
        assert(viewModel.description.value == description)
    }

    @Test
    fun `onWeightsChanged updates weights state`() {
        // Set weights
        val weights = "20.0"
        viewModel.onWeightsChanged(weights)

        // Assertions
        assert(viewModel.weights.value == weights)
    }

    @Test
    fun `onSetCountChanged updates setCount state`() {
        // Set set count
        val setCount = "4"
        viewModel.onSetCountChanged(setCount)

        // Assertions
        assert(viewModel.setCount.value == setCount)
    }

    @Test
    fun `onRepCountChanged updates repCount state`() {
        // Set rep count
        val repCount = "12"
        viewModel.onRepCountChanged(repCount)

        // Assertions
        assert(viewModel.repCount.value == repCount)
    }

    @Test
    fun `onRestTimeChanged updates restTime state`() {
        // Set rest time
        val restTime = "90.0"
        viewModel.onRestTimeChanged(restTime)

        // Assertions
        assert(viewModel.restTime.value == restTime)
    }
}
