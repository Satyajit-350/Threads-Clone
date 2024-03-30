package com.satyajit.threads.modals

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("to") val token: String = "",
    @SerializedName("notification") val notificationBody: NotificationBody
)