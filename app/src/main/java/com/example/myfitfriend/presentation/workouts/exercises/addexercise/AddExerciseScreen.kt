package com.example.myfitfriend.presentation.workouts.exercises.addexercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
@Composable
fun AddExerciseScreen(
    navController: NavController,
    viewModel: AddExerciseScreenViewModel = hiltViewModel(),
    workoutId: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Exercise") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Title field
            TextField(
                value = viewModel.title.value,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description field
            TextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // Weights field, accepts only doubles
            TextField(
                value = viewModel.weights.value,
                onValueChange = { viewModel.onWeightsChanged(it) },
                label = { Text("Weights (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Set count field, accepts only integers
            TextField(
                value = viewModel.setCount.value,
                onValueChange = { viewModel.onSetCountChanged(it) },
                label = { Text("Set Count") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Repetition count field, accepts only integers
            TextField(
                value = viewModel.repCount.value,
                onValueChange = { viewModel.onRepCountChanged(it) },
                label = { Text("Repetition Count") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Rest time field, accepts only doubles
            TextField(
                value = viewModel.restTime.value,
                onValueChange = { viewModel.onRestTimeChanged(it) },
                label = { Text("Rest Time (seconds)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Submit button
            Button(
                onClick = {
                    viewModel.onSubmitNewExercise(workoutId)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit")
            }

            // Observing submission success
            LaunchedEffect(viewModel.onSuccessfullyAdd.value) {
                if (viewModel.onSuccessfullyAdd.value) {
                    navController.navigateUp()
                }
            }
        }
    }
}
