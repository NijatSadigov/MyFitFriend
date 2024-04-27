package com.example.myfitfriend.presentation.dietarylogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myfitfriend.util.Screen

@Composable
fun DietaryLogsScreen (navController: NavController,
                       viewModel: DietaryLogsViewModel= hiltViewModel(),


){

LaunchedEffect(key1 = true) {
    viewModel.getDietaryLogs()
}

    Scaffold (
        topBar = {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Text(text="MyFitFriend")
                Text(text="Log out", modifier = Modifier.clickable { viewModel.logOut()
                navController.popBackStack()
                    navController.navigate(Screen.LoginScreen.route)
                })
                Text(text = "Profile Settings", Modifier.clickable {


                })


            }

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate((Screen.AddEditDietaryLogScreen.route))
            }) {
                Icon(imageVector = Icons.Default.Add , contentDescription = "Add meal")
            }
        }
    ){
        if(viewModel.dietaryLogs.value.isNotEmpty()){
            LazyColumn (modifier = Modifier.padding(it)){

            }
        }
        else{
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text="You don't have any meal:")
            }
        }
    }

}