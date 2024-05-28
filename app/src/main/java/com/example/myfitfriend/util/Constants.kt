package com.example.myfitfriend.util

object Constants {

    const val BASE_URL= "http://192.168.1.213:8080/"  // For Android Emulator

    val IGNORE_AUTH_URLS= listOf( "/login", "/register")
    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
    const val KEY_LOGGED_IN_EMAIL="KEY_LOGGED_IN_EMAIL"
    const val KEY_PASSWORD="KEY_PASSWORD"
    const val NO_EMAIL="NO_EMAIL"
    const val NO_PASSWORD="NO_PASSWORD"

}