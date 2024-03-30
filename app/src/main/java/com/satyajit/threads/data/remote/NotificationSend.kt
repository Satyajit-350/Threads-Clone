package com.satyajit.threads.data.remote

import com.satyajit.threads.modals.AuthResponse
import com.satyajit.threads.modals.Notification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationSend {
    @POST("fcm/send")
    suspend fun sendNotification(@Body notification: Notification): Response<AuthResponse>
}