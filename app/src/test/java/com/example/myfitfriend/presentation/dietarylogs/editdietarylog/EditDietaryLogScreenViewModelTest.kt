package com.example.myfitfriend.presentation.dietarylogs.editdietarylog

import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.UpdateDietaryLogsEntityUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class EditDietaryLogScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditDietaryLogScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val getFoodUseCase = GetFoodByIdUseCaseLB(fakeRepository)
        val updateDietaryLogUseCaseLB = UpdateDietaryLogsEntityUseCaseLB(fakeRepository)
        val getDietaryLogByIdUseCase = GetDietaryLogByIdUseCaseLB(fakeRepository)

        viewModel = EditDietaryLogScreenViewModel(
            getFoodUseCase,
            updateDietaryLogUseCaseLB,
            getDietaryLogByIdUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onScreenStartGetFood fetches dietary log and food details`() = runTest {
        // Mock data
        val dietaryLogId = 1
        val foodId = 1

        val foodEntity = LocalFoodEntity(foodId=foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        val dietaryLog = DietaryLogEntity(dietaryLogId = dietaryLogId,foodId = 1, lastEditDate = System.currentTimeMillis(), date= LocalDate.now().toString(), foodItem =  "apple", partOfDay =  1, amountOfFood =  100.0, userId = 1)
        fakeRepository.setDietaryLogs(listOf(dietaryLog))
        // Call the function
        viewModel.onScreenStartGetFood(dietaryLogId)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)


        // Assertions
        assert(viewModel.dietaryLogId.value == 1)
        assert(viewModel.amountOfFood.value == "100.0")

    }

    @Test
    fun `onAmountOfFoodChange updates food macros`() = runTest {
        // Mock data
        val foodId = 1
        val dietaryLogId = 1
        val dietaryLog =DietaryLogEntity(foodId = 1,lastEditDate =  System.currentTimeMillis(), date=LocalDate.now().toString(), partOfDay=1, foodItem =  "apple", dietaryLogId=dietaryLogId, amountOfFood =  100.0, userId = 1)
        fakeRepository.setDietaryLogs(listOf(dietaryLog))
        val foodEntity = LocalFoodEntity(foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        viewModel.onScreenStartGetFood(dietaryLogId)

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
        assert(viewModel.foodProtein.value == 28.0)
        assert(viewModel.foodFat.value == 0.6)
    }

    @Test
    fun `onSubmit updates dietary log`() = runTest {
        // Mock data
        val foodId = 1
        val dietaryLogId = 1
        val dietaryLog = DietaryLogEntity(dietaryLogId=1, foodId=foodId, amountOfFood =  100.0, date =  "2023-05-29", lastEditDate = System.currentTimeMillis(),foodItem= "Apple", partOfDay = 1, userId = 1)
        fakeRepository.setDietaryLogs(listOf(dietaryLog))
        val foodEntity = LocalFoodEntity(foodId, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        viewModel.onScreenStartGetFood(dietaryLogId)

        // Call the function
        viewModel.onSubmit()

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Successful Submission: ${viewModel.successfulSubmission.value}")

        // Assertions
        assert(viewModel.successfulSubmission.value)
        assert(fakeRepository.getDietaryLogsLB().isNotEmpty())
        val updatedLog = fakeRepository.getDietaryLogsLB().first()
        assert(updatedLog.foodId == foodId)
        assert(updatedLog.dietaryLogId == dietaryLogId)
        assert(updatedLog.amountOfFood == 100.0)
    }
}