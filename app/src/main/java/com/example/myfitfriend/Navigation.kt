package com.example.myfitfriend

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfitfriend.presentation.addeditdietarylog.AddEditDietaryLogScreen
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsScreen
import com.example.myfitfriend.presentation.login.LoginScreen
import com.example.myfitfriend.presentation.profile.ProfileScreen
import com.example.myfitfriend.presentation.register.RegisterScreen
import com.example.myfitfriend.presentation.showfoods.ShowFoodsScreen
import com.example.myfitfriend.util.Screen

@Composable
fun Navigation() {
    val navController= rememberNavController()

    NavHost(navController=navController, startDestination = Screen.LoginScreen.route)
    {
        composable(Screen.LoginScreen.route)
        {
            LoginScreen(navController=navController)
        }
        composable(Screen.RegisterScreen.route)
        {
            RegisterScreen(navController=navController)

        }
        composable(Screen.DietaryLogScreen.route)
        {
            DietaryLogsScreen(navController=navController)

        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen()
        }
        composable(Screen.EditProfileScreen.route+"?date={date}&?partOfDay={partOfDay}"){
            navBackStackEntry ->

        }
        composable(Screen.AddEditDietaryLogScreen.route+"?dietaryLogId={dietaryLogId}" +
                "&?foodItem={foodItem}" +
                "&?amountOfFood={amountOfFood}" +
                "&?foodId={foodId}" +
                "&?partOfDay={partOfDay}"
        )
        {
            navBackStackEntry ->
            AddEditDietaryLogScreen(navController=navController,
                dietaryLogId=  navBackStackEntry.arguments?.getInt("dietaryLogId"),
                foodItem= navBackStackEntry.arguments?.getString("foodItem"),
                amountOfFood=navBackStackEntry.arguments?.getDouble("amountOfFood"),
                foodId= navBackStackEntry.arguments?.getInt("foodId"),
                partOfDay= navBackStackEntry.arguments?.getInt("partOfDay"))

        }
        composable(Screen.ShowFoodsScreen.route){
            ShowFoodsScreen(navController=navController)

        }



    }

}