package com.satyajit.threads.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.satyajit.threads.MainActivity
import com.satyajit.threads.R
import com.satyajit.threads.presentation.home.HomeRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService: FirebaseMessagingService() {

    @Inject
    lateinit var homeRepository: HomeRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    
    @OptIn(ExperimentalSerializationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        var resultIntent: Intent? = null
        var pendingIntent: PendingIntent? = null

        val body = Json.decodeFromString<Map<String, String>>(message.notification?.body.toString())

        Log.d("Notification testing", body["messageBody"].toString())

        builder.setContentTitle(message.notification?.title)
        if (body["type"] == "FOLLOW") {
            builder.setContentText(body["messageBody"])
            resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra("type", "FOLLOW")
            pendingIntent =
                PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.setContentIntent(pendingIntent)
        }
        else if(body["type"] == "THREAD_UPLOAD"){
            builder.setContentText(body["messageBody"])
            resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra("type", "THREAD_UPLOAD")
            pendingIntent = PendingIntent.getActivity(this, 2, resultIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.setContentIntent(pendingIntent)
        }
        else if(body["type"] == "THREAD_REPLY"){
            builder.setContentText(body["messageBody"])
            resultIntent = Intent(this, MainActivity::class.java)
            pendingIntent = PendingIntent.getActivity(this, 3, resultIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.setContentIntent(pendingIntent)
        }else if(body["type"] == "THREAD_REPOST"){
            //TODO
        }
        builder.setSmallIcon(R.drawable.threads_logo)
        builder.setAutoCancel(true)

        val mNotificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "threads_notification_channel"
        val channel = NotificationChannel(
            channelId,
            "Threads Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        mNotificationManager.createNotificationChannel(channel)
        builder.setChannelId(channelId)
        mNotificationManager.notify(100, builder.build())
    }
    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            homeRepository.updateToken(token)
        }
    }
}