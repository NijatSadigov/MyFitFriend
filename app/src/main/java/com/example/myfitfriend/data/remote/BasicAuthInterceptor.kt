package com.example.myfitfriend.data.remote

import android.content.SharedPreferences
import com.example.myfitfriend.util.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            if (request.url.encodedPath in IGNORE_AUTH_URLS) {
                return chain.proceed(request)
            }

            val email = sharedPreferences.getString("email", "") ?: ""
            val password = sharedPreferences.getString("password", "") ?: ""

            val authenticationRequest = request.newBuilder()
                .header("Authorization", Credentials.basic(email, password))
                .build()

            return chain.proceed(authenticationRequest)
        }

}

