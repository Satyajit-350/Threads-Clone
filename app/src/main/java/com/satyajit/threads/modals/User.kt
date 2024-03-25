package com.satyajit.threads.modals

data class User(

    val name: String = "",
    val username: String = "",
    var userId: String= "",
    val email: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val bio: String = "",
    val links: List<String> = emptyList(),
    val location: String = "",
    val notificationToken: String = "",
    val profileType: Boolean = false
)

