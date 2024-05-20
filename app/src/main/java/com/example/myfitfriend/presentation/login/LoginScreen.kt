package com.example.myfitfriend.presentation.login
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(key1 = navController) {
        viewModel.isLoggedIn()
    }

    LaunchedEffect(key1 = viewModel.logInState.value) {
        if (viewModel.logInState.value) {
            navController.popBackStack()
            navController.navigate(Screen.DietaryLogScreen.route)
        }
    }

    LaunchedEffect(key1 = viewModel.toastEvent.value) {
        viewModel.toastEvent.value?.let { event ->
            when (event) {
                is ToastEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }

            delay(1500)
            viewModel._toastEvent.value = null  // Reset the toast event after showing
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

            Button(
                onClick = { viewModel.logIn() },
                enabled = viewModel.isButtonEnabled.value
            ) {
                Text(text = "Login", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Create Account", modifier = Modifier.clickable {
                navController.navigate(Screen.RegisterScreen.route)
            })
        }
    }
}