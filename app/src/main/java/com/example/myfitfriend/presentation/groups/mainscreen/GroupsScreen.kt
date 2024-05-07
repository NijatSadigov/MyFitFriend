package com.example.myfitfriend.presentation.groups.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.data.remote.reponses.DietGroup
import com.example.myfitfriend.util.Screen

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
                    TextButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                        Text("Profile", color = MaterialTheme.colors.onPrimary)
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
            selected = true,
            onClick = { /* Current Screen */ },
            label = { Text("Groups") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Groups Page") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.WorkoutScreen.route) },
            label = { Text("Workouts") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Workout Page") }
        )
    }
}

@Composable
fun GroupsList(groups: List<DietGroup>, ownerName: String, navController: NavController, viewModel: GroupsScreenViewModel, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(groups) { group ->
            GroupCard(
                group = group,
                ownerName = ownerName,
                onClick = { groupId ->
                    // navigate to specific group details
                }
            )
        }
    }
}

@Composable
fun GroupCard(group: DietGroup, ownerName: String, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(group.groupId) },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(group.groupName, style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(4.dp))
            Text("Owner: $ownerName", style = MaterialTheme.typography.body1)
        }
    }
}
