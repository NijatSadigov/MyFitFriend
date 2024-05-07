package com.example.myfitfriend.presentation.groups.addgroup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen

@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: CreateGroupScreenViewModel = hiltViewModel()
) {
    val groupName = viewModel.groupName.value
    val successfulCreation = viewModel.successfulCreation.value

    LaunchedEffect(successfulCreation) {
        if (successfulCreation) {
            // Navigate back to group listing or display success message
            navController.popBackStack()
            navController.navigate(Screen.GroupsScreen.route)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Group") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.groupName.value,
                onValueChange = { viewModel.onGroupNameChange(it) },
                label = { Text("Group Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.onGroupNameChange(viewModel.groupName.value)
                    viewModel.onCreateGroup()
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onCreateGroup()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.groupName.value.isNotBlank()
            ) {
                Text("Create Group")
                Icon(Icons.Filled.Check, contentDescription = "Create")
            }
        }
    }
}
