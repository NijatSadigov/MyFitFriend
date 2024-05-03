package com.example.myfitfriend.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.presentation.login.LoginViewModel
import com.example.myfitfriend.util.Screen
import kotlin.math.roundToInt


@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        viewModel.getProfileDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Fit Friend") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.userName.value,
                    onValueChange = { viewModel.updateUserName(it) },
                    label = { Text("User Name") }
                )
                OutlinedTextField(
                    value = viewModel.userWeight.value.toString(),
                    onValueChange = { viewModel.updateWeight(it.toDoubleOrNull() ?: viewModel.userWeight.value) },
                    label = { Text("Weight (kg)") }
                )
                OutlinedTextField(
                    value = viewModel.userHeight.value.toString(),
                    onValueChange = { viewModel.updateHeight(it.toDoubleOrNull() ?: viewModel.userHeight.value) },
                    label = { Text("Height (cm)") }
                )
                Text("Activity Level: ${viewModel.userActivityLevel.value}")
                Slider(
                    value = viewModel.userActivityLevel.value.toFloat(),
                    onValueChange = { viewModel.updateActivityLevel(it.roundToInt()) },
                    valueRange = 0f..3f,
                    steps = 2 // This makes the slider snap to values 0, 1, 2, 3
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.saveChanges()  // Assuming this function saves the profile data
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.height(8.dp))  // Provides visual separation between buttons
                Button(
                    onClick = {
                        viewModel.logOut()
                        navController.popBackStack()
                        navController.navigate(Screen.LoginScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out")
                }
            }
        }
    )

}