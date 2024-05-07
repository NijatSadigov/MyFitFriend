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
import com.example.myfitfriend.util.Screen

@Composable
fun SpecificGroupScreen(
    navController: NavController,
    viewModel: SpecificGroupScreenViewModel = hiltViewModel(),
    groupId:Int,
    groupOwner:String
) {

}
