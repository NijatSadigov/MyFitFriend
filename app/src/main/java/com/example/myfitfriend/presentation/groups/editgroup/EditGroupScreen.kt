package com.example.myfitfriend.presentation.groups.editgroup

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen


@Composable
fun EditGroupScreen(
    navController: NavController,
    viewModel: EditGroupScreenViewModel = hiltViewModel(),
    groupId: Int
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(true) {
        viewModel.getDietGroup(groupId)
    }
    val successfulEdition = viewModel.successfulEdition.value
    val messageToShow = viewModel.message.value

    LaunchedEffect(successfulEdition) {
        if (successfulEdition) {
            Toast.makeText(context, "Group edited successfully!", Toast.LENGTH_LONG).show()
            navController.navigate(Screen.GroupsScreen.route) {
                popUpTo(Screen.GroupsScreen.route) { inclusive = true }
            }
        } else if (messageToShow.isNotEmpty()) {
            Toast.makeText(context, messageToShow, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("Edit Group") })
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
                    viewModel.onEditGroup(groupId)
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEditGroup(groupId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.groupName.value.isNotBlank()
            ) {
                Text("Change Group Name")
                Icon(Icons.Filled.Check, contentDescription = "Change")
            }
        }
    }
}