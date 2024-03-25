package com.satyajit.threads.navigation

sealed class Routes(val route: String) {

    data object Auth: Routes("Auth")
    data object Login: Routes("Login")
    data object Register: Routes("Register")
    data object SaveInfo: Routes("SaveInfo")
    data object ProfileInfo: Routes("ProfileInfo")
    data object PrivacyInfo: Routes("PrivacyInfo")
    data object FollowInfo: Routes("FollowInfo")
    data object WorkingInfo: Routes("WorkingInfo")
    data object Home: Routes("Home")
    data object Splash: Routes("Splash")
    data object Notification: Routes("Notification")
    data object Profile: Routes("Profile")
    data object Search: Routes("Search")
    data object AddThreads: Routes("AddThreads")
    data object Likes: Routes("Likes")
    data object BottomNav: Routes("BottomNavigation")
    data object Settings: Routes("Settings")
    data object Privacy: Routes("Privacy")
    data object Mentions: Routes("Mentions")
    data object EditProfile: Routes("EditProfile")
    data object EditBio: Routes("EditBio")
}