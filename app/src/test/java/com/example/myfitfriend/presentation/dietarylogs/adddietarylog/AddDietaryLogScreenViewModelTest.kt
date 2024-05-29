package com.example.myfitfriend.presentation.dietarylogs.adddietarylog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.UserEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.InsertDietaryLogsEntityUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class AddDietaryLogScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddDietaryLogScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val getFoodUseCase = GetFoodByIdUseCaseLB(fakeRepository)
        val insertDietaryLogsEntityUseCaseLB = InsertDietaryLogsEntityUseCaseLB(fakeRepository)
        val getUserUseCaseLB = GetUserUseCaseLB(fakeRepository)

        viewModel = AddDietaryLogScreenViewModel(
            getFoodUseCase,
            insertDietaryLogsEntityUseCaseLB,
            getUserUseCaseLB
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onScreenStart fetches food and user details`() = runTest {
        // Mock data
        val foodId = 1
        val foodEntity = LocalFoodEntity(foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))

        val userEntity = UserEntity(activityLevel = 1, username="John Doe",weight= 75.0,height= 180.0,age= 25,sex=true, email = "nicat@mail.com", lastEditDate = System.currentTimeMillis(), passwordHash = "password", userId = 1)
        fakeRepository.addAndEditUser(userEntity)

        // Call the function
        viewModel.onScreenStart(foodId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Food Name: ${viewModel.foodName.value}")
        println("User ID: ${viewModel.userId.value}")

        // Assertions
        assert(viewModel.foodName.value == "Apple")
        assert(viewModel.foodCal.value == 52.0)
        assert(viewModel.foodCarb.value == 0.2)
        assert(viewModel.foodProtein.value == 14.0)
        assert(viewModel.foodFat.value == 0.3)
        assert(viewModel.userId.value == 1)
    }

    @Test
    fun `onAmountOfFoodChange updates food macros`() = runTest {
        // Mock data
        val foodId = 1
        val foodEntity = LocalFoodEntity(foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        viewModel.onScreenStart(foodId)

        // Call the function
        viewModel.onAmountOfFoodChange("200")

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Food Calories: ${viewModel.foodCal.value}")
        println("Food Carbs: ${viewModel.foodCarb.value}")
        println("Food Protein: ${viewModel.foodProtein.value}")
        println("Food Fat: ${viewModel.foodFat.value}")

        // Assertions
        assert(viewModel.foodCal.value == 104.0)
        assert(viewModel.foodCarb.value == 0.4)
        assert(viewModel.foodProtein.value ==28.0 )
        assert(viewModel.foodFat.value == 0.6)
    }

    @Test
    fun `onSubmit adds dietary log`() = runTest {
        // Mock data
        val foodId = 1
        val userId = 1
        val foodEntity = LocalFoodEntity(foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        val userEntity = UserEntity(activityLevel = 1, username="John Doe",weight= 75.0,height= 180.0,age= 25,sex=true, email = "nicat@mail.com", lastEditDate = System.currentTimeMillis(), passwordHash = "password", userId = 1)
        fakeRepository.addAndEditUser(userEntity)
        viewModel.onScreenStart(foodId)

        // Call the function
        viewModel.onSubmit(foodId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Successful Submission: ${viewModel.successfulSubmission.value}")

        // Assertions
        assert(viewModel.successfulSubmission.value)
        assert(fakeRepository.getDietaryLogsLB().isNotEmpty())
        assert(fakeRepository.getDietaryLogsLB().first().foodId == foodId)
        assert(fakeRepository.getDietaryLogsLB().first().userId == userId)
    }
}