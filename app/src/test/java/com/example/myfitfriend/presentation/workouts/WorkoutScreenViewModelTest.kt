package com.example.myfitfriend.presentation.workouts
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.DeletedItemsEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.workout.DeleteWorkoutByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutsUseCaseLB
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository

import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
import com.example.myfitfriend.presentation.workouts.WorkoutScreenViewModel
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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class WorkoutScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WorkoutScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val workoutsUseCase = GetWorkoutsUseCaseLB(fakeRepository)
        val deleteWorkoutByIdUseCase = DeleteWorkoutByIdUseCaseLB(fakeRepository)
        val insertDeletionTableUseCaseLB = InsertDeletionTableUseCaseLB(fakeRepository)

        viewModel = WorkoutScreenViewModel(
            workoutsUseCase,
            deleteWorkoutByIdUseCase,
            insertDeletionTableUseCaseLB
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWorkouts fetches and updates workout list`() = runTest {
        // Mock data
        val workoutList = listOf(
            WorkoutEntity(System.currentTimeMillis(), title = "Workout 1", description = "Description 1", date = LocalDate.now().toString(), userId = 1, workoutId = 1),
            WorkoutEntity(System.currentTimeMillis(), title = "Workout 2", description = "Description 2",date = LocalDate.now().toString(), userId = 1, workoutId = 2)
        )
        fakeRepository.setAllWorkouts(workoutList)

        // Mock the flow
        val mockFlow = flow {
            emit(Resources.Success(workoutList))
        }
        val workoutsUseCase = mock(GetWorkoutsUseCaseLB::class.java)
        `when`(workoutsUseCase()).thenReturn(mockFlow)

        // Call the function
        viewModel.getWorkouts()

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Workouts: ${viewModel.workouts.value}")

        // Assertions
        assert(viewModel.workouts.value.size == 2)
        assert(viewModel.workouts.value[0].workoutId == 1)
        assert(viewModel.workouts.value[1].workoutId == 2)
    }

    @Test
    fun `onDelete deletes workout and updates UI`() = runTest {
        // Mock data
        val workoutList = listOf(
            WorkoutEntity(System.currentTimeMillis(), title = "Workout 1", description = "Description 1", date = LocalDate.now().toString(), userId = 1, workoutId = 1),
            WorkoutEntity(System.currentTimeMillis(), title = "Workout 2", description = "Description 2",date = LocalDate.now().toString(), userId = 1, workoutId = 2)
        )
        fakeRepository.setAllWorkouts(workoutList)


        // Call the function
        viewModel.onDelete(1, true)
        viewModel.onDelete(2, true)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Workouts after delete: ${viewModel.workouts.value}")
        println("Deletion Table: ${fakeRepository.getDeletionTable()}")

        // Assertions
        assert(viewModel.workouts.value.isEmpty())
        assert(fakeRepository.getDeletionTable().isNotEmpty())
    }
}
