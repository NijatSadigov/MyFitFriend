package com.example.myfitfriend.presentation.workouts.exercises.addexercise
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.domain.use_case.exercise.CreateExerciseUseCaseLB
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
class AddExerciseScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddExerciseScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val addExerciseToWorkoutUseCase = CreateExerciseUseCaseLB(fakeRepository)

        viewModel = AddExerciseScreenViewModel(addExerciseToWorkoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSubmitNewExercise adds exercise successfully`() = runTest {
        // Mock data
        val workoutId = 1
        val exerciseEntity = ExerciseEntity(
            exerciseId = 0,
            workoutId = workoutId,
            title = "Push Ups",
            description = "Do 3 sets of push ups",
            weights = 0.0,
            setCount = 3,
            repCount = 15,
            restTime = 60.0,
            lastEditDate = System.currentTimeMillis()
        )


        // Set ViewModel state
        viewModel.onTitleChanged("Push Ups")
        viewModel.onDescriptionChanged("Do 3 sets of push ups")
        viewModel.onWeightsChanged("0.0")
        viewModel.onSetCountChanged("3")
        viewModel.onRepCountChanged("15")
        viewModel.onRestTimeChanged("60.0")

        // Call the function
        viewModel.onSubmitNewExercise(workoutId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("OnSuccessfullyAdd: ${viewModel.onSuccessfullyAdd.value}")

        // Assertions
        assert(viewModel.onSuccessfullyAdd.value)
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
