package com.example.myfitfriend.presentation.groups.mainscreen

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.util.Screen
import kotlinx.coroutines.delay

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsScreenViewModel = hiltViewModel()
) {
    val dietGroups = viewModel.dietGroups.value
    val groupOwnerName = viewModel.groupOwnerName.value

    LaunchedEffect(key1 = Unit) {
        viewModel.getGroups()
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
            ExtendedFloatingActionButton(
                text = { Text("Add Group") },
                onClick = { navController.navigate(Screen.CreateGroupScreen.route)},
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = false,
        bottomBar = {
            GroupsBottomBar(navController)
        }
    ) { innerPadding ->
        GroupsList(dietGroups, groupOwnerName, navController, viewModel, Modifier.padding(innerPadding))
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
            onClick = { /* Current Screen */ },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups Page") }
        )
    }
}
@Composable
fun GroupsList(
    groups: List<DietGroup>,
    ownerNames: List<String>, // List of owner names
    navController: NavController,
    viewModel: GroupsScreenViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        itemsIndexed(groups) { index, group -> // Use itemsIndexed to get both index and item
            val ownerName = ownerNames.getOrNull(index) ?: "Unknown Owner" // Default to "Unknown Owner" if null
            GroupCard(
                group = group,
                ownerName = ownerName,
                navController = navController,
                viewModel = viewModel,
                onDeleteConfirmed = {
                    viewModel.deleteGroup(group.groupId)
                }
            )
        }
    }
}

@Composable
fun GroupCard(group: DietGroup, ownerName: String, navController: NavController, viewModel: GroupsScreenViewModel, onDeleteConfirmed: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this group?") },
            confirmButton = {
                Button(onClick = {
                    onDeleteConfirmed(group.groupId)
                    showDialog = false
                    if(viewModel.deletionSuccess.value)
                    showSnackbar = true
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

    if (showSnackbar) {
        LaunchedEffect(key1 = Unit) {
            delay(3000) // Show the snackbar for 3 seconds
            showSnackbar = false
        }
        Snackbar {
            Text("Group deleted successfully")
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
                Spacer(Modifier.height(4.dp))
                Text("Owner: $ownerName", style = MaterialTheme.typography.body1)
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
