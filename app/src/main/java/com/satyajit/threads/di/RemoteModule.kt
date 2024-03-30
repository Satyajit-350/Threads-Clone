package com.satyajit.threads.di

import com.satyajit.threads.data.remote.NotificationSend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteModule {
    @Provides
    @Singleton
    fun providesNotificationAPI(client: OkHttpClient) = Retrofit.Builder()
        .client(client)
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NotificationSend::class.java)

    @Provides
    @Singleton
    fun provideAuthInterceptor() = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization", "key=AAAA_mNxH8M:APA91bE2d5Ldo923PwRu2uAKmOMpOQhkSAqwGzm8pT3oo1EoPWI_jCmWk0FR2aDKyZeJsucj9NTHQ6-rvjY3yjJA7sxxifaEllJRVqwFTXmJOZPCgOx4_yAmJXLLYJwISqRjmCbFqMrX")
            .build()
        chain.proceed(newRequest)
    }).build()
}