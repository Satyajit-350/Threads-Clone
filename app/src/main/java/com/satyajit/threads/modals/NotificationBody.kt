package com.satyajit.threads.modals

import kotlinx.serialization.Serializable

@Serializable
data class NotificationBody(
    val title: String,
    val body: String
)
