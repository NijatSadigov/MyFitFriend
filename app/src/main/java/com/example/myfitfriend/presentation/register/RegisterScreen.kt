package com.example.myfitfriend.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
import kotlin.math.roundToInt

@Composable
fun RegisterScreen(navController: NavController,viewModel: RegisterViewModel= hiltViewModel()){
    LaunchedEffect(key1 = viewModel.isRegistered.value) {
        if(viewModel.isRegistered.value){
            navController.navigateUp()

        }
    }
    Scaffold{
        Column(
            Modifier
                .padding(16.dp)
                .padding(it)){
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Welcome Back" , fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(modifier = Modifier.fillMaxWidth() ,
                value = viewModel.emailState.value,
                onValueChange =viewModel::onEmailChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "Email") }
            )
            TextField(modifier = Modifier.fillMaxWidth() ,
                value = viewModel.passwordState.value,
                onValueChange =viewModel::onPasswordChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(modifier = Modifier.fillMaxWidth() ,
                value = viewModel.userName.value,
                onValueChange =viewModel::onUserNameChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Name") }
            )
            ////height
            Spacer(modifier = Modifier.height(16.dp))
            TextField(modifier = Modifier.fillMaxWidth() ,
                value = viewModel.userHeight.value,
                onValueChange =viewModel::onUserHeightChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Height") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            TextField(modifier = Modifier.fillMaxWidth() ,
                value = viewModel.userWeight.value,
                onValueChange =viewModel::onUserWeightChange,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                label = { Text(text = "User Weight") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            val activityLevelLabels = listOf("Sedentary", "Light Exercise", "Medium Exercise", "Hard Exercise")
            val activityLevel = viewModel.userActivityLevel.value
            Column {
                Text("User Activity Level: ${activityLevelLabels[activityLevel]}", fontSize = 18.sp)
                Slider(
                    value = activityLevel.toFloat(),
                    onValueChange = { viewModel.onUserActivityLevelChange(it.roundToInt()) },
                    valueRange = 0f..3f,
                    steps = 2,  // This creates three intervals (0-1-2-3)
                    onValueChangeFinished = {
                        // You might want to react when the user has finished dragging the slider
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.onRegister() }) {
                Text(text = "Register", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "AlreadyHaveAccount?" , modifier = Modifier.clickable{
                navController.navigate(Screen.LoginScreen.route)
            })


        }

    }

}