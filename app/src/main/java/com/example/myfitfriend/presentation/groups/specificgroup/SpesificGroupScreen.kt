package com.example.myfitfriend.presentation.groups.specificgroup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.getGroupMembersLogs(groupId)
        viewModel.getGroup(groupId)
    }

    LaunchedEffect(viewModel.showToast.value) {
        viewModel.showToast.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

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
                    onClick = { navController.navigate(Screen.GroupsScreen.route) },
                    label = { Text("Groups") },
                    icon = { Icon(Icons.Default.Face, contentDescription = "Groups Page") }
                )
            }
        }
    ) { innerPadding ->
        GroupContent(viewModel, Modifier.padding(innerPadding), navController, groupId)
    }
}

@Composable
fun GroupContent(viewModel: SpecificGroupScreenViewModel, modifier: Modifier, navController: NavController, groupId: Int) {
    Column(modifier = modifier.padding(8.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Group: ${viewModel.groupName.value}", style = MaterialTheme.typography.h6)
                Text("Owner: ${viewModel.groupOwnerName.value}", style = MaterialTheme.typography.body1)
                Text("Members: ${viewModel.memberLogs.value.size}", style = MaterialTheme.typography.body2)
            }
        }

        // Invite section
        if (viewModel.areUOwner.value) {
            Spacer(Modifier.height(8.dp))
            InviteMemberForm(viewModel, groupId)
        }

        Spacer(Modifier.height(8.dp))
        MemberList(viewModel, navController, groupId)
    }
}

@Composable
fun InviteMemberForm(viewModel: SpecificGroupScreenViewModel, groupId: Int) {
    var userEmailText by remember { mutableStateOf("") }

    // A function to validate and set the userIdText
    fun updateUserIdText(input: String) {

            userEmailText = input

    }

    Column {
        TextField(
            value = userEmailText,
            onValueChange = { updateUserIdText(it) },
            label = { Text("Enter user email to invite") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true  // Ensures the TextField doesn't wrap to a new line
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.inviteMemberByEmail(userEmailText, groupId)
            },
            modifier = Modifier.align(Alignment.End),
            enabled = userEmailText.isNotEmpty()  // Ensure that the button is only clickable if the text is not empty
        ) {
            Text("Invite")
        }
    }
}

@Composable
fun MemberList(viewModel: SpecificGroupScreenViewModel, navController: NavController, groupId: Int) {
    LazyColumn {
        items(viewModel.memberLogs.value) { member ->
            MemberCard(member, viewModel, navController, groupId)
        }
    }
}

@Composable
fun MemberCard(member: GroupDietaryLogsItem, viewModel: SpecificGroupScreenViewModel, navController: NavController, groupId: Int) {
    val isCurrentUser = member.userId == viewModel.userId.value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        backgroundColor = if (isCurrentUser) Color.Green else Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${member.userName}: ${member.totalCalories.toInt()}/${member.maxCalories.toInt()} kcal", style = MaterialTheme.typography.h6)
            Text("Protein: ${member.totalProtein.toInt()}g")
            Text("Carb: ${member.totalCarb.toInt()}g")
            Text("Fat: ${member.totalFat.toInt()}g")
            if (viewModel.areUOwner.value && !isCurrentUser) {
                Button(onClick = {
                    viewModel.kickUser(member.userId, groupId = groupId)
                }) {
                    Text("Kick")
                }
            }
        }
    }
}