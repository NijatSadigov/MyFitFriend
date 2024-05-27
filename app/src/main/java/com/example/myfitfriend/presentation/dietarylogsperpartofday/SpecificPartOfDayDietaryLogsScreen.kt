package com.example.myfitfriend.presentation.dietarylogsperpartofday

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsBottomBar
import com.example.myfitfriend.presentation.dietarylogsperpartofday.SpecificPartOfDayDietaryLogsScreenViewModel
import com.example.myfitfriend.util.Screen

@Composable
fun SpecificPartOfDayDietaryLogsScreen(
    navController: NavController,
    viewModel: SpecificPartOfDayDietaryLogsScreenViewModel = hiltViewModel(),
    partOfDay: Int
) {
    LaunchedEffect(key1 = true) {
        viewModel.getDietaryLogs(partOfDay)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dietary Logs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { DietaryLogsBottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            FoodListSection(viewModel,navController, Modifier.weight(1f),partOfDay)
            NutritionalInfoSection(viewModel, Modifier.weight(1f))
        }
    }
}
@Composable
fun FoodListSection(viewModel: SpecificPartOfDayDietaryLogsScreenViewModel, navController: NavController,modifier: Modifier,partOfDay: Int) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(viewModel.dietaryLogsFrame.value) { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                elevation = 4.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = food.foodName, style = MaterialTheme.typography.h6)
                        Text(text = "Cal: ${food.cal}  Carb: ${food.carb}g  Protein: ${food.protein}g  Fat: ${food.fat}g")
                    }
                    IconButton(onClick = {
                        viewModel.onDeleteDietaryLog(partOfDay =partOfDay, dietaryLogId = food.dietaryLogId, isAdded = food.isAdded )
                    /* Implement delete functionality */ }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = { /* Implement edit functionality */
                    navController.navigate(Screen.EditDietaryLogScreen.route+"?dietaryLogId=${food.dietaryLogId}")
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionalInfoSection(viewModel: SpecificPartOfDayDietaryLogsScreenViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Total Calories: ${viewModel.totalCalories.value}", style = MaterialTheme.typography.h6)
        Text("Total Carbs: ${viewModel.totalCarbs.value}g", style = MaterialTheme.typography.body1)
        Text("Total Protein: ${viewModel.totalProtein.value}g", style = MaterialTheme.typography.body1)
        Text("Total Fats: ${viewModel.totalFats.value}g", style = MaterialTheme.typography.body1)
        //println("Debug: Calories = ${viewModel.totalCalories.value}")

    }
}
