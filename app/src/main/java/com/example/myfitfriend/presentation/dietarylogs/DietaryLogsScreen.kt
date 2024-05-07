package com.example.myfitfriend.presentation.dietarylogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsViewModel
import com.example.myfitfriend.util.Screen


@Composable
fun DietaryLogsScreen(navController: NavController, viewModel: DietaryLogsViewModel = hiltViewModel()) {
    // Launch effect to load dietary logs when the component is first composed
    LaunchedEffect(key1 = true) {
        viewModel.getDietaryLogs()
        //viewModel.getDietaryLogByDateAndPartOfDay()
    }

    // Main scaffold with top app bar and floating action button
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "MyFitFriend") }, actions = {

                TextButton(onClick = {
                    navController.navigate(Screen.ProfileScreen.route) // Navigate to profile settings screen
                }) {
                    Text("Profile Settings", color = MaterialTheme.colors.onPrimary)
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditDietaryLogScreen.route) // Navigate to add/edit meal screen
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add meal")
            }
        },
        bottomBar = { DietaryLogsBottomBar(navController) }


    ) { paddingValues ->
        Row(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            NutritionalInfoSection(viewModel = viewModel)
            Spacer(modifier = Modifier.width(16.dp))
            MealButtonsSection(viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun NutritionalInfoSection(viewModel: DietaryLogsViewModel) {
    Column(modifier = Modifier.width(200.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Total Calories: ${viewModel.totalCalories.value}", style = MaterialTheme.typography.h6)
        Text("Total Carbs: ${viewModel.totalCarbs.value}g", style = MaterialTheme.typography.body1)
        Text("Total Protein: ${viewModel.totalProtein.value}g", style = MaterialTheme.typography.body1)
        Text("Total Fats: ${viewModel.totalFats.value}g", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun MealButtonsSection(viewModel: DietaryLogsViewModel, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Button for breakfast, showing total calories for this meal type
      MealButton("Breakfast", viewModel.totalBreakfastCalories.value , navController, Screen.BreakfastScreen.route)
        // Button for lunch, showing total calories for this meal type
       MealButton("Lunch", viewModel.totalLunchCalories.value, navController, Screen.LunchScreen.route)
        // Button for dinner, showing total calories for this meal type
       MealButton("Dinner", viewModel.totalDinnerCalories.value, navController, Screen.DinnerScreen.route)
        // Button for snack, showing total calories for this meal type
        MealButton("Snack", viewModel.totalSnackCalories.value, navController, Screen.SnackScreen.route)
    }
}

@Composable
fun MealButton(mealType: String, calories: Double, navController: NavController, route: String) {
    Button(onClick = { navController.navigate(route) }) { // Navigate to specific meal screen on button click
        Column {
            Text(mealType, style = MaterialTheme.typography.subtitle1) // Show meal type
            Text("${calories} kcal", style = MaterialTheme.typography.body2) // Show calories for the meal
        }
    }
}
@Composable
fun DietaryLogsBottomBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            selected = true,
            onClick = { /* Current Screen */ },
            label = { Text("Diet") },
            icon = { Icon(Icons.Default.List, contentDescription = "Diet Page") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.WorkoutScreen.route) },
            label = { Text("Workouts") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Workout Page") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.GroupsScreen.route)
                },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups") }
        )
    }
}



