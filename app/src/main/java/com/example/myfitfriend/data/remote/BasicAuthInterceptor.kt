package com.example.myfitfriend.data.remote

import com.example.myfitfriend.util.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {
    var email: String? = null
    var password: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath in IGNORE_AUTH_URLS) {
            return chain.proceed(request)
        }

        val authenticationRequest = request.newBuilder()
            .header("Authorization", Credentials.basic(email ?: "", password ?: ""))
            .build()

        return chain.proceed(authenticationRequest)
    }
}
