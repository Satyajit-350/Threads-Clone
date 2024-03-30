package com.satyajit.threads.services

import android.util.Log
import com.satyajit.threads.data.remote.NotificationSend
import com.satyajit.threads.modals.Notification
import javax.inject.Inject

class FCMNotificationRepository @Inject constructor(
    private val notificationSend: NotificationSend
) {
    suspend fun sendNotification(notification: Notification){
        try{
            val response = notificationSend.sendNotification(notification)
            if(response.isSuccessful){
                Log.d("sendNotification",
                    "sendNotification: ${notification.token} \n ${notification.notificationBody}")
            }else{
                Log.d("sendNotification", "sendNotificationError: ${response.errorBody().toString()}")
            }

        }catch (e:Exception){
            e.printStackTrace()
            Log.d("sendNotification", "sendNotificationException: $e")
        }
    }

}