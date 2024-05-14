package com.example.myfitfriend.presentation.groups.invites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.data.remote.reponses.AddFriendRequestInfo
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.presentation.groups.mainscreen.GroupsBottomBar
import com.example.myfitfriend.util.Screen
import kotlinx.coroutines.delay

@Composable
fun InvitesScreen(
    navController: NavController,
    viewModel: InvitesScreenViewModel = hiltViewModel()
) {
    val invites = viewModel.invites.value

    LaunchedEffect(key1 = Unit) {
        viewModel.getInvites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invites") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = false,
        bottomBar = {
            GroupsBottomBar(navController)
        }
    ) { innerPadding ->
        InvitesList(invites, viewModel, Modifier.padding(innerPadding))
    }
}

@Composable
fun InvitesList(invites: List<AddFriendRequestInfo>, viewModel: InvitesScreenViewModel, modifier: Modifier) {
    if (invites.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Invites")
        }
    } else {
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            items(invites) { invite ->
                InviteCard(invite, viewModel)
            }
        }
    }
}

@Composable
fun InviteCard(invite: AddFriendRequestInfo, viewModel: InvitesScreenViewModel) {
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    if (showToast) {
        LaunchedEffect(key1 = toastMessage) {
            delay(2000) // Show toast message for 2 seconds
            viewModel.getInvites()

            showToast = false

        }
        Toast(toastMessage)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(invite.groupName, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {
                        viewModel.answerInvite(true, invite.requestId)

                        toastMessage = "Accepted invite to ${invite.groupName}"
                        showToast = true

                    }
                ) {
                    Text("Accept")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.answerInvite(false, invite.requestId)
                        toastMessage = "Rejected invite to ${invite.groupName}"
                        showToast = true
                    }
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
fun Toast(message: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Snackbar { Text(message) }
    }
}