package com.example.myfitfriend.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.presentation.login.LoginViewModel
import com.example.myfitfriend.util.Screen
import kotlin.math.roundToInt

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun showToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        viewModel.getProfileDetails()
    }

    val context = LocalContext.current  // Get the context for the Toast

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
                    onValueChange = viewModel::updateUserName,
                    label = { Text("User Name") }
                )
                OutlinedTextField(
                    value = viewModel.userWeight.value.toString(),
                    onValueChange = { weight -> viewModel.updateWeight(weight.toDoubleOrNull() ?: viewModel.userWeight.value) },
                    label = { Text("Weight (kg)") }
                )
                OutlinedTextField(
                    value = viewModel.userHeight.value.toString(),
                    onValueChange = { height -> viewModel.updateHeight(height.toDoubleOrNull() ?: viewModel.userHeight.value) },
                    label = { Text("Height (cm)") }
                )
                Text("Activity Level: ${viewModel.userActivityLevel.value}")
                Slider(
                    value = viewModel.userActivityLevel.value.toFloat(),
                    onValueChange = { viewModel.updateActivityLevel(it.roundToInt()) },
                    valueRange = 0f..3f,
                    steps = 2 // This makes the slider snap to values 0, 1, 2, 3
                )
                Text("Age: ${viewModel.userAge.value}", fontSize = 18.sp)
                Slider(
                    value = viewModel.userAge.value.toFloat(),
                    onValueChange = { viewModel.onAgeChange(it.roundToInt()) },
                    valueRange = 0f..100f,
                    steps = 82, // Enables fine-grained age selection
                    onValueChangeFinished = {}
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { viewModel.onSexChange(true) },
                        modifier = Modifier.weight(1f).padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (viewModel.userSex.value) Color.Blue else Color.LightGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Male")
                    }
                    Button(
                        onClick = { viewModel.onSexChange(false) },
                        modifier = Modifier.weight(1f).padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (!viewModel.userSex.value) Color.Blue else Color.LightGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Female")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.saveChanges {
                            Toast.makeText(context, "Profile edited successfully!", Toast.LENGTH_SHORT).show()
                        }
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