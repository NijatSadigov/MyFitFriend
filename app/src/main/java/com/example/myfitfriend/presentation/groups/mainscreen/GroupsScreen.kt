package com.example.myfitfriend.presentation.groups.mainscreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.connectivity.NetworkConnectivityObserver
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.util.Screen
import kotlinx.coroutines.delay

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsScreenViewModel = hiltViewModel(),
    connectivityObserver: ConnectivityObserver
) {
    val dietGroups = viewModel.dietGroups.value
    val status by connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    )
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getGroups()
    }

    LaunchedEffect(viewModel.deletionError.value) {
        viewModel.deletionError.value?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.resetDeletionSuccessState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Fit Friend") },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.InvitesScreen.route) }) {
                        Text("Invites", color = MaterialTheme.colors.onPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            if (status == ConnectivityObserver.Status.Available) {
                ExtendedFloatingActionButton(
                    text = { Text("Add Group") },
                    onClick = { navController.navigate(Screen.CreateGroupScreen.route) },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = false,
        bottomBar = {
            GroupsBottomBar(navController)
        }
    ) { innerPadding ->
        if (status == ConnectivityObserver.Status.Available) {
            GroupsList(dietGroups, navController, viewModel, Modifier.padding(innerPadding))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("There is no connection, please have connection first", style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

@Composable
fun GroupCard(group: DietGroup, navController: NavController, viewModel: GroupsScreenViewModel, onDeleteConfirmed: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this group?") },
            confirmButton = {
                Button(onClick = {
                    onDeleteConfirmed(group.groupId)
                    showDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(viewModel.deletionSuccess.value) {
        if (viewModel.deletionSuccess.value) {
            Toast.makeText(context, "Group deleted successfully", Toast.LENGTH_SHORT).show()
            viewModel.resetDeletionSuccessState()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("${Screen.SpecificGroupScreen.route}?groupId=${group.groupId}") },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(group.groupName, style = MaterialTheme.typography.h6)
            }
            Row {
                IconButton(onClick = { navController.navigate("${Screen.EditGroupScreen.route}?groupId=${group.groupId}") }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun GroupsBottomBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.DietaryLogScreen.route) },
            label = { Text("Diet") },
            icon = { Icon(Icons.Default.List, contentDescription = "Diet Page") }
        )

        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.WorkoutScreen.route) },
            label = { Text("Workouts") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Workout Page") }
        )
        BottomNavigationItem(
            selected = true,
            onClick = { navController.navigate(Screen.GroupsScreen.route) },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups Page") }
        )
    }
}

@Composable
fun GroupsList(
    groups: List<DietGroup>,
    navController: NavController,
    viewModel: GroupsScreenViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(groups) { group ->
            GroupCard(
                group = group,
                navController = navController,
                viewModel = viewModel,
                onDeleteConfirmed = {
                    viewModel.deleteGroup(group.groupId)
                }
            )
        }
    }
}

