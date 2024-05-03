package com.example.myfitfriend.presentation.workouts.editworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.presentation.workouts.addworkout.AppBottomBar
import com.example.myfitfriend.presentation.workouts.addworkout.AppTopBar
import com.example.myfitfriend.presentation.workouts.editworkout.EditWorkoutScreenViewModel

@Composable
fun EditWorkoutScreen(
    navController: NavController,
    workoutId: Int,
    viewModel: EditWorkoutScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getCurrentWorkoutDetails(workoutId)
    }


    Scaffold(
        topBar = { AppTopBar(navController) },
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        EditWorkoutContent(navController, workoutId, viewModel, Modifier.padding(innerPadding))
    }
}

@Composable
fun EditWorkoutContent(
    navController: NavController,
    workoutId: Int,
    viewModel: EditWorkoutScreenViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val title = viewModel.editWorkoutTitle.value
        val description = viewModel.editWorkoutDescription.value

        if (viewModel.workoutCreated.value) {
            LaunchedEffect(Unit) {
                navController.popBackStack() // Assuming you want to navigate back after updating
            }
        }

        TextField(
            value = title,
            onValueChange = { viewModel.onEditWorkoutTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { viewModel.onEditWorkoutDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.onEditWorkout(workoutId) })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onEditWorkout(workoutId) },
            modifier = Modifier.align(Alignment.End),
            enabled = title.isNotEmpty() && description.isNotEmpty()
        ) {
            Text("Update")
        }
    }
}

// AppTopBar and AppBottomBar definitions remain the same as in your provided code
