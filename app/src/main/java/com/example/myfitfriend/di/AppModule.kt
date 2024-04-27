package com.example.myfitfriend.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.MyFitFriendAPI
import com.example.myfitfriend.data.repository.MyFitFriendRepositoryIMPL
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.util.Constants.BASE_URL
import com.example.myfitfriend.util.Constants.ENCRYPTED_SHARED_PREF_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.annotation.Signed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBasicAuthInterceptor()=BasicAuthInterceptor()

    @Singleton
    @Provides
    fun provideMyFitFriendApi(
        basicAuthInterceptor: BasicAuthInterceptor

    ):MyFitFriendAPI{
        val client = OkHttpClient.Builder().addInterceptor(basicAuthInterceptor).build()
        return Retrofit.Builder().
        baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).
        client(client).build().create(MyFitFriendAPI::class.java)


    }
    @Singleton
    @Provides
    fun provideMyFitFriendRepository(api: MyFitFriendAPI):MyFitFriendRepository=MyFitFriendRepositoryIMPL(api)

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context):SharedPreferences{
        val masterKey= MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,

            )
    }





}