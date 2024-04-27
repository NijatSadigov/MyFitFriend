package com.example.myfitfriend.data.remote.requests

import java.sql.Date

data class GetDietaryLogRequest(
    val date: String,
    val partOfDay:Int
)
