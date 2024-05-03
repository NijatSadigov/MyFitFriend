package com.example.myfitfriend.presentation.workouts.spesific

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.MyFitFriend.data.model.Exercise
import com.example.myfitfriend.util.Screen
@Composable
fun SpecificWorkoutScreen(
    navController: NavController,
    viewModel: SpecificWorkoutScreenViewModel = hiltViewModel(),
    workoutId: String?
) {
    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        SpecificWorkoutContent(navController, viewModel, workoutId, Modifier.padding(innerPadding))
    }
}

@Composable
fun SpecificWorkoutContent(
    navController: NavController,
    viewModel: SpecificWorkoutScreenViewModel,
    workoutId: String?,
    modifier: Modifier
) {
    val exercises = viewModel.exercises.value

    Column(modifier = modifier.fillMaxSize()) {
        workoutId?.toIntOrNull()?.let { id ->
            LaunchedEffect(key1 = id) {
                viewModel.getExercises(id)
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items = exercises, key = { exercise -> exercise.exerciseId }) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onEditClicked = {
                            navController.navigate("${Screen.EditExerciseScreen.route}?workoutId=${workoutId}&?exerciseId=${exercise.exerciseId}")
                        },
                        onDeleteClicked = { ex ->
                            viewModel.onDeleteExercise(ex.exerciseId, id)
                            println("Delete clicked for ${ex.exerciseId}")
                        }
                    )
                }
            }

            // Add Exercise button
            Button(
                onClick = { navController.navigate("${Screen.AddExerciseScreen.route}?workoutId=$workoutId") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add Exercise")
            }
        } ?: println("Invalid workout ID")
    }
}


@Composable
fun ExerciseCard(
    exercise: Exercise,
    onEditClicked: (Exercise) -> Unit,
    onDeleteClicked: (Exercise) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = exercise.title, style = MaterialTheme.typography.h3)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = exercise.description, style = MaterialTheme.typography.h4)
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "Duration: ${exercise.restTime} min", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Weights: ${exercise.weights} kg", style = MaterialTheme.typography.h6)
                    Text(text = "Sets: ${exercise.setCount}", style = MaterialTheme.typography.h6)
                    Text(text = "Reps: ${exercise.repCount}", style = MaterialTheme.typography.h6)
                }
            }
            Row {
                Button(
                    onClick = { onEditClicked(exercise) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onDeleteClicked(exercise) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete")
                }
            }
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
            onClick = { //navController.navigate(Screen.DietGroupsScreen.route)
            },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups") }
        )
    }
}
