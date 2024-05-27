package com.example.myfitfriend

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.connectivity.NetworkConnectivityObserver
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsScreen
import com.example.myfitfriend.presentation.dietarylogs.Foods.BarcodeScannerScreen
import com.example.myfitfriend.presentation.groups.addgroup.CreateGroupScreen
import com.example.myfitfriend.presentation.groups.mainscreen.GroupsScreen
import com.example.myfitfriend.presentation.login.LoginScreen
import com.example.myfitfriend.presentation.profile.ProfileScreen
import com.example.myfitfriend.presentation.register.RegisterScreen
import com.example.myfitfriend.presentation.dietarylogs.Foods.ShowFoodsScreen
import com.example.myfitfriend.presentation.dietarylogs.adddietarylog.AddDietaryLogScreen
import com.example.myfitfriend.presentation.dietarylogs.editdietarylog.EditDietaryLogScreen
import com.example.myfitfriend.presentation.dietarylogsperpartofday.SpecificPartOfDayDietaryLogsScreen
import com.example.myfitfriend.presentation.groups.editgroup.EditGroupScreen
import com.example.myfitfriend.presentation.groups.invites.InvitesScreen
import com.example.myfitfriend.presentation.groups.specificgroup.SpecificGroupScreen
import com.example.myfitfriend.presentation.workouts.WorkoutScreen
import com.example.myfitfriend.presentation.workouts.addworkout.CreateWorkoutScreen
import com.example.myfitfriend.presentation.workouts.editworkout.EditWorkoutScreen
import com.example.myfitfriend.presentation.workouts.exercises.addexercise.AddExerciseScreen
import com.example.myfitfriend.presentation.workouts.exercises.editexercise.EditExerciseScreen
import com.example.myfitfriend.presentation.workouts.spesific.SpecificWorkoutScreen
import com.example.myfitfriend.util.Screen

@Composable
fun Navigation(applicationContext:Context) {
    val navController= rememberNavController()
    lateinit var connectivityObserver: ConnectivityObserver

    connectivityObserver= NetworkConnectivityObserver(applicationContext)


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
            DietaryLogsScreen(navController=navController, connectivityObserver = connectivityObserver)

        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen(navController=navController,connectivityObserver=connectivityObserver)
        }
        /////////////////////Workout and exercises
        composable(Screen.WorkoutScreen.route)
        {
            WorkoutScreen(navController = navController)
        }
        composable(Screen.AddWorkoutScreen.route)
        {
            CreateWorkoutScreen(navController = navController)
        }
        composable(Screen.EditDietaryLogScreen.route+"?dietaryLogId={dietaryLogId}"){
                navBackStackEntry ->
            EditDietaryLogScreen(navController=navController, dietaryLogId =navBackStackEntry.arguments?.getString("dietaryLogId")!!.toInt())
        }


        composable(Screen.SpecificWorkoutScreen.route+"?workoutId={workoutId}"){
            navBackStackEntry ->
            SpecificWorkoutScreen(navController = navController, workoutId = navBackStackEntry.arguments?.getString("workoutId"))
        }
        composable(Screen.EditWorkoutScreen.route+"?workoutId={workoutId}"){
                navBackStackEntry ->
            EditWorkoutScreen(navController = navController, workoutId = navBackStackEntry.arguments?.getString("workoutId")!!.toInt())
        }

//        composable(Screen.EditProfileScreen.route+"?date={date}&?partOfDay={partOfDay}"){
//            navBackStackEntry ->
//
//        }
//        composable(Screen.AddEditDietaryLogScreen.route+"?dietaryLogId={dietaryLogId}" +
//                "&?foodItem={foodItem}" +
//                "&?amountOfFood={amountOfFood}" +
//                "&?foodId={foodId}" +
//                "&?partOfDay={partOfDay}"
//        )
//        {
//            navBackStackEntry ->
//            AddEditDietaryLogScreen(navController=navController,
//                dietaryLogId=  navBackStackEntry.arguments?.getInt("dietaryLogId"),
//                foodItem= navBackStackEntry.arguments?.getString("foodItem"),
//                amountOfFood=navBackStackEntry.arguments?.getDouble("amountOfFood"),
//                foodId= navBackStackEntry.arguments?.getInt("foodId"),
//                partOfDay= navBackStackEntry.arguments?.getInt("partOfDay"))
//
//        }

        composable(Screen.ShowFoodsScreen.route){
            ShowFoodsScreen(navController=navController)

        }
        composable(Screen.BarcodeScannerScreen.route){
            BarcodeScannerScreen(navController=navController)
        }
        composable(Screen.AddDietaryLogScreen.route+"?foodId={foodId}"){
            navBackStackEntry ->
            val foodId=navBackStackEntry.arguments!!.getString("foodId")!!.toInt()

                AddDietaryLogScreen(navController = navController, foodId =foodId)



        }

        composable(Screen.SpecificPartOfDayDietaryLogScreen.route +"?partOfDay={partOfDay}"){
        navBackStackEntry ->
           val partOfDay =navBackStackEntry.arguments?.getString("partOfDay")
           // println("navigation $partOfDay")
            SpecificPartOfDayDietaryLogsScreen(navController = navController, partOfDay=partOfDay!!.toInt() )


        }
//        composable(Screen.ExercisesScreen.route){
//
//        }
        composable(Screen.AddExerciseScreen.route+"?workoutId={workoutId}"){
                navBackStackEntry ->

        AddExerciseScreen(navController = navController,  workoutId = navBackStackEntry.arguments?.getString("workoutId")!!.toInt())
        }
        composable(Screen.EditWorkoutScreen.route+"?workoutId={workoutId}"){
                navBackStackEntry ->
            EditWorkoutScreen(navController = navController, workoutId = navBackStackEntry.arguments?.getString("workoutId")!!.toInt())

        }
        composable(Screen.EditExerciseScreen.route + "?workoutId={workoutId}&exerciseId={exerciseId}") {
                navBackStackEntry ->
            EditExerciseScreen(
                navController = navController,
                workoutId = navBackStackEntry.arguments?.getString("workoutId")?.toIntOrNull() ?: -1,
                exerciseId = navBackStackEntry.arguments?.getString("exerciseId")?.toIntOrNull() ?: -1
            )
        }

        composable(Screen.GroupsScreen.route){

            GroupsScreen(navController=navController,connectivityObserver=connectivityObserver)
        }
        composable(Screen.CreateGroupScreen.route){
            CreateGroupScreen(navController = navController)
        }
        composable(Screen.EditGroupScreen.route+"?groupId={groupId}"){
            navBackStackEntry->
            EditGroupScreen(navController = navController, groupId = navBackStackEntry.arguments?.getString("groupId")!!.toIntOrNull() ?: -1)
        }
        composable(Screen.SpecificGroupScreen.route+"?groupId={groupId}")
        {
            navBackStackEntry ->
            SpecificGroupScreen(navController = navController, groupId =navBackStackEntry.arguments?.getString("groupId")!!.toIntOrNull() ?: -1 )
        }
        composable(Screen.InvitesScreen.route){
            InvitesScreen(navController = navController)
        }

//        // object GroupsScreen:Screen("groups")  //shows users already joined groups , and on the top of page  a button to move to addgroup screen and invites button which also shows current number of invites and when u click it it should go invitesScreen
//    //there are cards which shows users group name, owner name, description and group id
//    object CurrentGroupScreen:Screen("current_group")
//
//    object AddGroupScreen:Screen("create_group") //
   //  object EditGroupScreen:Screen("editGroup") //avaiable for only owners
//    object InviteUserToGroupScreen:Screen("invite_user_to_group")
//    object CurrentInvitesScreen:Screen("invites_of_user")






    }

}