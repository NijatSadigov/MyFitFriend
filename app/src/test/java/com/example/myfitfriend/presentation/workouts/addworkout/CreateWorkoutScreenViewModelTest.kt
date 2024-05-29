package com.example.myfitfriend.presentation.workouts.addworkout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.CreateWorkoutEntityUseCaseLB
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository

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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class CreateWorkoutScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateWorkoutScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val createWorkoutUseCase = CreateWorkoutEntityUseCaseLB(fakeRepository)
        val getUserUseCaseLB = GetUserUseCaseLB(fakeRepository)

        viewModel = CreateWorkoutScreenViewModel(createWorkoutUseCase, getUserUseCaseLB)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onCreateWorkout creates workout successfully`() = runTest {
        // Mock data
        val workoutEntity = WorkoutEntity(
            workoutId = 0,
            title = "New Workout",
            description = "Workout Description",
            lastEditDate = System.currentTimeMillis(),
            date = LocalDate.now().toString(),
            userId = 1
        )

        fakeRepository.setUser(UserEntity(activityLevel = 1, username="John Doe",weight= 75.0,height= 180.0,age= 25,sex=true, email = "nicat@mail.com", lastEditDate = System.currentTimeMillis(), passwordHash = "password", userId = 1)
        )


        // Set ViewModel state
        viewModel.onCreateWorkoutTitle("New Workout")
        viewModel.onCreateWorkoutDescription("Workout Description")

        // Call the function
        viewModel.onCreateWorkout()

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Workout Created: ${viewModel.workoutCreated.value}")

        // Assertions
        assert(viewModel.workoutCreated.value)
    }

    @Test
    fun `onCreateWorkoutTitle updates title state`() {
        // Set title
        val title = "New Workout"
        viewModel.onCreateWorkoutTitle(title)

        // Assertions
        assert(viewModel.createWorkoutTitle.value == title)
    }

    @Test
    fun `onCreateWorkoutDescription updates description state`() {
        // Set description
        val description = "Workout Description"
        viewModel.onCreateWorkoutDescription(description)

        // Assertions
        assert(viewModel.createWorkoutDescription.value == description)
    }
}
