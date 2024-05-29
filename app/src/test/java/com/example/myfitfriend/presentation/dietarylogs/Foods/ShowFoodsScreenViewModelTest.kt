package com.example.myfitfriend.presentation.dietarylogs.Foods
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myfitfriend.data.local.LocalFoodEntity
import com.example.myfitfriend.data.local.asFoodResponse
import com.example.myfitfriend.data.local.domain.use_case.foods.GetFoodsUseCaseLB
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.data.repository.FakeMyFitFriendLocalRepository
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
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class ShowFoodsScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShowFoodsScreenViewModel
    private lateinit var fakeRepository: FakeMyFitFriendLocalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepository = FakeMyFitFriendLocalRepository()

        // Use case instances using fake repository
        val showFoodsUseCase = GetFoodsUseCaseLB(fakeRepository)

        viewModel = ShowFoodsScreenViewModel(
            showFoodsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onGetFoods fetches and updates food list`() = runTest {
        // Mock data
        val foodList = listOf(
            LocalFoodEntity(1, "Apple", 52.0, 14.0, 0.2, 0.3, "12345"),
            LocalFoodEntity(2, "Banana", 89.0, 23.0, 1.1, 0.3, "67890")
        )
        fakeRepository.setFoods(foodList)

        // Mock the flow
        val mockFlow = flow {
            emit(Resources.Success(foodList))
        }
        val showFoodsUseCase = mock(GetFoodsUseCaseLB::class.java)
        `when`(showFoodsUseCase()).thenReturn(mockFlow)

        viewModel = ShowFoodsScreenViewModel(showFoodsUseCase)

        // Call the function
        viewModel.onGetFoods()

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Check state
        println("Foods: ${viewModel.foods.value}")
        println("Filtered Foods: ${viewModel.filteredFoods.value}")

        // Assertions
        assert(viewModel.foods.value.size == 2)
        assert(viewModel.foods.value[0].foodName == "Apple")
        assert(viewModel.foods.value[1].foodName == "Banana")
        assert(viewModel.filteredFoods.value.size == 2)
    }

    @Test
    fun `onSearchBarChange filters food list`() = runTest {
        // Mock data
        val foodList = listOf(
            LocalFoodEntity(1, "Apple", 52.0, 14.0, 0.2, 0.3, "12345"),
            LocalFoodEntity(2, "Banana", 89.0, 23.0, 1.1, 0.3, "67890")
        )
        fakeRepository.setFoods(foodList)

        // Mock the flow
        val mockFlow = flow {
            emit(Resources.Success(foodList))
        }
        val showFoodsUseCase = mock(GetFoodsUseCaseLB::class.java)
        `when`(showFoodsUseCase()).thenReturn(mockFlow)

        viewModel = ShowFoodsScreenViewModel(showFoodsUseCase)

        // Call the function to fetch foods first
        viewModel.onGetFoods()

        // Add some delay to ensure the coroutines finish processing
        kotlinx.coroutines.delay(1000)

        // Now filter the foods
        viewModel.onSearchBarChange("Banana")

        // Check state
        println("Filtered Foods after search: ${viewModel.filteredFoods.value}")

        // Assertions
        assert(viewModel.filteredFoods.value.size == 1)
        assert(viewModel.filteredFoods.value[0].foodName == "Banana")
    }
}
