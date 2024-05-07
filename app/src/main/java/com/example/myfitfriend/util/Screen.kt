package com.example.myfitfriend.util

sealed class Screen(val route:String) {
    object LoginScreen:Screen("login_screen")
    object RegisterScreen:Screen("register_screen")
    object DietaryLogScreen:Screen("dietary_log_screen")
    object AddEditDietaryLogScreen:Screen("add_edit_dietary_log_screen")
    object ProfileScreen:Screen("profile")
    object EditProfileScreen:Screen("edit_profile")
    object ShowFoodsScreen:Screen("show_foods")

    object BreakfastScreen:Screen("breakfast")
    object LunchScreen:Screen("lunch")
    object DinnerScreen :Screen("dinner")
    object SnackScreen:Screen("snack")

    //workouts
    object WorkoutScreen:Screen("workouts")

    object SpecificWorkoutScreen : Screen("specific_workout")
    object EditWorkoutScreen:Screen("edit_workout")
    object AddWorkoutScreen:Screen("add_workout")
    //object ExercisesScreen:Screen("exercises")
    object AddExerciseScreen:Screen("add_exercise")
    object EditExerciseScreen:Screen("edit_exercise")

    //groups
    object GroupsScreen:Screen("groups")  //shows users already joined groups , and on the top of page  a button to move to addgroup screen and invites button which also shows current number of invites and when u click it it should go invitesScreen
    //there are cards which shows users group name, owner name, description and group id
    object SpecificGroupScreen:Screen("current_group")

    object CreateGroupScreen:Screen("create_group") //
    object EditGroupScreen:Screen("editGroup") //avaiable for only owners
    object InviteUserToGroupScreen:Screen("invite_user_to_group")
    object CurrentInvitesScreen:Screen("invites_of_user")



}