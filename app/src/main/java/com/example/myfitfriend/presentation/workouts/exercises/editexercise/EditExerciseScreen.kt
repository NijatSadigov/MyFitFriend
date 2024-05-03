package com.example.myfitfriend.presentation.workouts.exercises.editexercise

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
fun EditExerciseScreen(
    navController: NavController,
    editExerciseScreenViewModel: EditExerciseScreenViewModel = hiltViewModel(),
    workoutId: Int,
    exerciseId: Int
) {
    LaunchedEffect(key1 = exerciseId) {
        editExerciseScreenViewModel.getCurrentDetails(exerciseId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Exercise") },
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
                value = editExerciseScreenViewModel.title.value,
                onValueChange = editExerciseScreenViewModel::onTitleChanged,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description field
            TextField(
                value = editExerciseScreenViewModel.description.value,
                onValueChange = editExerciseScreenViewModel::onDescriptionChanged,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // Weights field, accepts only doubles
            TextField(
                value = editExerciseScreenViewModel.weights.value,
                onValueChange = editExerciseScreenViewModel::onWeightsChanged,
                label = { Text("Weights (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Set count field, accepts only integers
            TextField(
                value = editExerciseScreenViewModel.setCount.value,
                onValueChange = editExerciseScreenViewModel::onSetCountChanged,
                label = { Text("Set Count") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Repetition count field, accepts only integers
            TextField(
                value = editExerciseScreenViewModel.repCount.value,
                onValueChange = editExerciseScreenViewModel::onRepCountChanged,
                label = { Text("Repetition Count") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Rest time field, accepts only doubles
            TextField(
                value = editExerciseScreenViewModel.restTime.value,
                onValueChange = editExerciseScreenViewModel::onRestTimeChanged,
                label = { Text("Rest Time (seconds)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Submit button
            Button(
                onClick = {
                    editExerciseScreenViewModel.onSubmitEditedExercise(workoutId, exerciseId)
                    if (editExerciseScreenViewModel.onSuccessfullyEdit.value) {
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit")
            }

            // Observing submission success
            LaunchedEffect(editExerciseScreenViewModel.onSuccessfullyEdit.value) {
                if (editExerciseScreenViewModel.onSuccessfullyEdit.value) {
                    navController.navigateUp()
                }
            }
        }
    }
}
