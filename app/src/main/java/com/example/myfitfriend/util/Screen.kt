package com.example.myfitfriend.util

sealed class Screen(val route:String) {
    object LoginScreen:Screen("login_screen")
    object RegisterScreen:Screen("register_screen")
    object DietaryLogScreen:Screen("dietary_log_screen")
    object AddEditDietaryLogScreen:Screen("add_edit_dietary_log_screen")
    object ProfileScreen:Screen("profile")
    object EditProfileScreen:Screen("edit_profile")
}