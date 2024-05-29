package com.example.myfitfriend.presentation.dietarylogsperpartofday

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.DeleteDietaryLogByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.GetDietaryLogsByDateAndIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodByIdUseCaseLB
import com.example.myfitfriend.data.remote.reponses.DietaryLogFrameItem
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository
import com.example.myfitfriend.domain.use_case.sync.delete.InsertDeletionTableUseCaseLB
import com.example.myfitfriend.presentation.dietarylogsperpartofday.SpecificPartOfDayDietaryLogsScreenViewModel
import com.example.myfitfriend.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SpecificPartOfDayDietaryLogsScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SpecificPartOfDayDietaryLogsScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val getFoodUseCase = GetFoodByIdUseCaseLB(fakeRepository)
        val getDietaryLogByDateAndPartOfDayUseCase = GetDietaryLogsByDateAndIdUseCaseLB(fakeRepository)
        val deleteDietaryByIdLogUseCaseLB = DeleteDietaryLogByIdUseCaseLB(fakeRepository)
        val insertDeletionTableUseCaseLB = InsertDeletionTableUseCaseLB(fakeRepository)

        viewModel = SpecificPartOfDayDietaryLogsScreenViewModel(
            getFoodUseCase,
            getDietaryLogByDateAndPartOfDayUseCase,
            deleteDietaryByIdLogUseCaseLB,
            insertDeletionTableUseCaseLB
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getDietaryLogs fetches logs and calculates total macros`() = runTest {
        // Mock data
        val partOfDay = 1
        val dietaryLogs = listOf(DietaryLogEntity(foodId = 1, lastEditDate = System.currentTimeMillis(), date=LocalDate.now().toString(), foodItem =  "apple", partOfDay =  partOfDay, amountOfFood =  100.0, userId = 1))
        fakeRepository.setDietaryLogs(dietaryLogs)

        val foodEntity = LocalFoodEntity(1, "Apple", 52.0, 14.0, 0.2, 0.3, "12345")
        fakeRepository.setFoods(listOf(foodEntity))
        println("fakerepositoryLogs ${fakeRepository.getDietaryLogsLB()}")
        // Call the function
        viewModel.getDietaryLogs(partOfDay)
        println(viewModel.totalCalories.value)
        println(viewModel.totalCarbs.value)
        println(viewModel.totalProtein.value)
        println(viewModel.totalFats.value)
        // Check state
        assert(viewModel.totalCalories.value == 52.0)
        assert(viewModel.totalCarbs.value == 0.2)
        assert(viewModel.totalProtein.value == 14.0)
        assert(viewModel.totalFats.value == 0.3)
    }

    @Test
    fun `onDeleteDietaryLog deletes log and updates UI`() = runTest {
        // Mock data
        val partOfDay = 1
        val dietaryLogId = 1
        val isAdded = true
        val dietaryLogs = listOf(DietaryLogEntity(foodId = 1,lastEditDate =  System.currentTimeMillis(), date=LocalDate.now().toString(), partOfDay=partOfDay, foodItem =  "apple", dietaryLogId=dietaryLogId, amountOfFood =  100.0, userId = 1))
        fakeRepository.setDietaryLogs(dietaryLogs)

        // Call the function
        viewModel.onDeleteDietaryLog(partOfDay, dietaryLogId, isAdded)

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state and print debugging information
        println("Dietary Logs: ${viewModel.dietaryLogs.value}")
        println("Deletion Table: ${fakeRepository.getDeletionTable()}")

        // Assertions
        assert(viewModel.dietaryLogs.value.isEmpty())
        assert(fakeRepository.getDeletionTable().isNotEmpty())
        assert(fakeRepository.getDeletionTable().first().deletedId == dietaryLogId)
    }
}