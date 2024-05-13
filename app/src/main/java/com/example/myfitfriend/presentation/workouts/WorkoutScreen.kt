package com.example.myfitfriend.presentation.workouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.util.Screen

import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.automirrored.filled.List  // Use the specific icon import

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person

@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutScreenViewModel = hiltViewModel()
) {
    val workouts = viewModel.workouts.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        viewModel.getWorkouts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Fit Friend") },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                        Text("Profile", color = MaterialTheme.colors.onPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Workout") },
                onClick = { navController.navigate(Screen.AddWorkoutScreen.route)},
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = false,
        bottomBar = {
            WorkoutBottomBar(navController)
        }
    ) { innerPadding ->
        WorkoutList(workouts, navController, viewModel, Modifier.padding(innerPadding))
    }
}
@Composable
fun WorkoutBottomBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.DietaryLogScreen.route) },
            label = { Text("Diet") },
            icon = { Icon(Icons.Default.List, contentDescription = "Diet Page") }
        )
        BottomNavigationItem(
            selected = true,
            onClick = { /* Current Screen */ },
            label = { Text("Workouts") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Workout Page") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.GroupsScreen.route)
                      },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups") }
        )
    }
}

@Composable
fun WorkoutList(workouts: List<Workout>, navController: NavController, viewModel: WorkoutScreenViewModel, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(workouts) { workout ->
            WorkoutCard(
                workout = workout,
                onClick = { workoutId ->
                    navController.navigate("${Screen.SpecificWorkoutScreen.route}?workoutId=$workoutId")
                },
                onEdit = { workoutId ->
                    navController.navigate("${Screen.EditWorkoutScreen.route}?workoutId=$workoutId")
                },
                onDelete = { workoutId ->
                    viewModel.onDelete(workoutId)
                }
            )
        }
    }
}




@Composable
fun WorkoutCard(workout: Workout, onClick: (Int) -> Unit, onEdit: (Int) -> Unit, onDelete: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(workout.workoutId) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(workout.title, style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(4.dp))
                Text(workout.description, style = MaterialTheme.typography.body2)
            }
            Row {
                IconButton(onClick = { onEdit(workout.workoutId) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Workout")
                }
                IconButton(onClick = { onDelete(workout.workoutId) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Workout")
                }
            }
        }
    }
}

