package com.example.myfitfriend.util

sealed class Screen(val route:String) {
    object LoginScreen:Screen("login_screen")
    object RegisterScreen:Screen("register_screen")
    object DietaryLogScreen:Screen("dietary_log_screen")
    object AddEditDietaryLogScreen:Screen("add_edit_dietary_log_screen")
    object ProfileScreen:Screen("profile")

    object ShowFoodsScreen:Screen("show_foods")
    object AddDietaryLogScreen:Screen("add_dietary_log_food")
    object SpecificPartOfDayDietaryLogScreen:Screen("specific_part_of_day_dietary_log_screen")
    object EditDietaryLogScreen:Screen("add_dietary_log_food")


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
    object InvitesScreen:Screen("invites_of_user")



}