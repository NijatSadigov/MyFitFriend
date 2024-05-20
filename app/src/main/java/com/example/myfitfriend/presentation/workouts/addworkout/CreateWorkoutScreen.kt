package com.example.myfitfriend.presentation.workouts.addworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
@Composable
fun CreateWorkoutScreen(
    navController: NavController,
    viewModel: CreateWorkoutScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        CreateWorkoutContent(navController, viewModel, Modifier.padding(innerPadding))
    }
}
@Composable
fun CreateWorkoutContent(
    navController: NavController,
    viewModel: CreateWorkoutScreenViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val title = viewModel.createWorkoutTitle.value
        val description = viewModel.createWorkoutDescription.value
        val workoutCreated= viewModel.workoutCreated.value

        if (workoutCreated) {
            LaunchedEffect(Unit) {
                navController.popBackStack() // Assuming you want to navigate back on creation
            }
        }

        TextField(
            value = title,
            onValueChange = { viewModel.onCreateWorkoutTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { viewModel.onCreateWorkoutDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.onCreateWorkout() })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onCreateWorkout() },
            modifier = Modifier.align(Alignment.End),
            enabled = title.isNotEmpty() && description.isNotEmpty()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun AppTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("My Fit Friend") },
        actions = {
            TextButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                Text("Profile", color = MaterialTheme.colors.onPrimary)
            }
        }
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.DietaryLogScreen.route) },
            label = { Text("Diet") },
            icon = { Icon(Icons.Default.List, contentDescription = "Diet Page") }
        )
        BottomNavigationItem(
            selected = true,
            onClick = { navController.navigate(Screen.WorkoutScreen.route) },
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