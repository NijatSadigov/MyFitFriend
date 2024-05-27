package com.example.myfitfriend.presentation.dietarylogs.adddietarylog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsBottomBar


    @Composable
    fun AddDietaryLogScreen(navController: NavController,
                            viewModel: AddDietaryLogScreenViewModel = hiltViewModel(),
                            foodId: Int
    ) {
        LaunchedEffect(key1 = true) {
            viewModel.onScreenStart(foodId)
        //viewModel.getDietaryLogByDateAndPartOfDay()
        }

        LaunchedEffect(key1 = viewModel.successfulSubmission.value) {
            if (viewModel.successfulSubmission.value) {
                navController.navigate(Screen.DietaryLogScreen.route) {
                    // Optional: Add these lines if you want to pop up to a particular screen in your backstack or clear backstack
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Dietary Log") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    }
                )
            },
            bottomBar = { DietaryLogsBottomBar(navController) }
        ) { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .padding(8.dp)) {
                FoodDetailsSection(viewModel)
                AmountOfFoodInput(viewModel)
                PartOfDayButtons(viewModel)
                SubmitButton(viewModel, foodId)
            }
        }
    }


@Composable
fun FoodDetailsSection(viewModel: AddDietaryLogScreenViewModel) {
    Column {
        Text("Food: ${viewModel.foodName.value}", style = MaterialTheme.typography.h6)
        Text("Calories: ${viewModel.foodCal.value} kcal", style = MaterialTheme.typography.body1)
        Text("Protein: ${viewModel.foodProtein.value} g", style = MaterialTheme.typography.body1)
        Text("Carbs: ${viewModel.foodCarb.value} g", style = MaterialTheme.typography.body1)
        Text("Fats: ${viewModel.foodFat.value} g", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun AmountOfFoodInput(viewModel: AddDietaryLogScreenViewModel) {
    var text by remember { mutableStateOf(viewModel.amountOfFood.value) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it.filter { it.isDigit() }
            viewModel.onAmountOfFoodChange(text)
        },
        label = { Text("Amount in grams") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun PartOfDayButtons(viewModel: AddDietaryLogScreenViewModel) {
    val partOfDay = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        partOfDay.forEachIndexed { index, part ->
            val isSelected = viewModel.partOfDay.value == index
            Button(
                onClick = { viewModel.onPartOfDayChange(index) },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)
            ) {
                Text(part)
            }
        }
    }
}

@Composable
fun SubmitButton(viewModel: AddDietaryLogScreenViewModel, foodId: Int) {
    Button(
        onClick = { viewModel.onSubmit(foodId)

                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = viewModel.partOfDay.value != -1  // Disabled if no part of day is selected
    ) {
        Icon(Icons.Filled.Check, contentDescription = "Submit")
        Text("Submit")
    }
}


