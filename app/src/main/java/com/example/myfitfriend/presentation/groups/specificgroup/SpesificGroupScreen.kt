package com.example.myfitfriend.presentation.groups.specificgroup

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
import com.example.myfitfriend.data.remote.reponses.GroupDietaryLogsItem
import com.example.myfitfriend.util.Screen


@Composable
fun SpecificGroupScreen(
    navController: NavController,
    viewModel: SpecificGroupScreenViewModel = hiltViewModel(),
    groupId: Int
) {
    LaunchedEffect(true) {
        viewModel.getGroupMembers(groupId)
    }
    LaunchedEffect(viewModel.membersId) {
        viewModel.getGroup(groupId)
        viewModel.getUserId()

    }
    LaunchedEffect(viewModel.userId) {

        viewModel.checkAreUOwner(userId = viewModel.userId.value, ownerId = viewModel.groupOwnerId.value)

    }
    LaunchedEffect(viewModel.membersId.value.size==viewModel.memberUserNames.value.size) {
        viewModel.getLogsOfMembers()

    }
    println(viewModel.userId.value)

    println(viewModel.areUOwner.value)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.groupName.value) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
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
    ) { innerPadding ->
        GroupContent(viewModel, Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun GroupContent(viewModel: SpecificGroupScreenViewModel, modifier: Modifier, navController: NavController) {
    Column(modifier = modifier.padding(8.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Group: ${viewModel.groupName.value}", style = MaterialTheme.typography.h6)
                Text("Owner: ${viewModel.groupOwnerName.value}", style = MaterialTheme.typography.body1)
                Text("Members: ${viewModel.membersId.value.size}", style = MaterialTheme.typography.body2)
            }
        }
        Spacer(Modifier.height(8.dp))
        MemberList(viewModel, navController)
    }
}

@Composable
fun MemberList(viewModel: SpecificGroupScreenViewModel, navController: NavController) {
    LazyColumn {
        items(viewModel.memberLogs.value) { member ->
            MemberCard(member, viewModel, navController)
        }
    }
}

@Composable
fun MemberCard(member: GroupDietaryLogsItem, viewModel: SpecificGroupScreenViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${member.userName}: ${member.totalCalories}/${member.maxCalories} kcal", style = MaterialTheme.typography.h6)
            Text("Protein: ${member.totalProtein}g")
            Text("Carb: ${member.totalCarb}g")
            Text("Fat: ${member.totalFat}g")
            if (viewModel.areUOwner.value) {
                Button(onClick = {
                   // viewModel.kickUser(member.userId, groupId = groupId)
                }) {
                    Text("Kick")
                }
            }
        }
    }
}
