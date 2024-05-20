package com.example.myfitfriend.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
import kotlin.math.roundToInt

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.isRegistered.value) {
        if (viewModel.isRegistered.value) {
            navController.navigateUp()
        }
    }
    Scaffold {
        Column(
            Modifier
                .padding(16.dp)
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Welcome Back", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.emailState.value,
                onValueChange = viewModel::onEmailChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "Email") }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.passwordState.value,
                onValueChange = viewModel::onPasswordChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.userName.value,
                onValueChange = viewModel::onUserNameChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.userHeight.value,
                onValueChange = { if (it.isNullOrBlank() || it.toDoubleOrNull() != null) viewModel.onUserHeightChange(it) },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Height (in cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.userWeight.value,
                onValueChange = { if (it.isNullOrBlank() || it.toDoubleOrNull() != null) viewModel.onUserWeightChange(it) },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Weight (in kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                val age = viewModel.userAge.value
                Text("User Age: $age", fontSize = 18.sp)
                Slider(
                    value = age.toFloat(),
                    onValueChange = { viewModel.onAgeChange(it.roundToInt()) },
                    valueRange = 0f..100f,
                    steps = 82,
                    onValueChangeFinished = {}
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            val activityLevelLabels = listOf("Sedentary", "Light Exercise", "Medium Exercise", "Hard Exercise")
            val activityLevel = viewModel.userActivityLevel.value
            Column {
                Text("User Activity Level: ${activityLevelLabels[activityLevel]}", fontSize = 18.sp)
                Slider(
                    value = activityLevel.toFloat(),
                    onValueChange = { viewModel.onUserActivityLevelChange(it.roundToInt()) },
                    valueRange = 0f..3f,
                    steps = 2
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { viewModel.onSexChange(true) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (!viewModel.userSex.value) Color.Blue else Color.LightGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Female")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onRegister(context) },
                enabled = viewModel.isRegisterButtonEnabled.value
            ) {
                Text(text = "Register", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Already Have an Account?", modifier = Modifier.clickable {
                navController.navigate(Screen.LoginScreen.route)
            })
        }
    }
}



