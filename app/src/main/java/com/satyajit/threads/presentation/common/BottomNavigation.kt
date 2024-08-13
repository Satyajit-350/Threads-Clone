package com.satyajit.threads.presentation.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.satyajit.threads.connectivity.ConnectivityStatus
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.add_thread.AddThreadScreen
import com.satyajit.threads.presentation.home.HomeScreen
import com.satyajit.threads.presentation.notification.NotificationScreen
import com.satyajit.threads.presentation.profile.ProfileScreen
import com.satyajit.threads.presentation.search.SearchScreen
import com.satyajit.threads.connectivity.ConnectionState
import com.satyajit.threads.utils.connectivityState

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BottomNavigation(navHostController: NavHostController) {

    val navController = rememberNavController()
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    Scaffold(
        bottomBar = {
            Column(
               modifier = Modifier.fillMaxWidth()
            ) {
                ConnectivityStatus(isConnected = isConnected)
                MyNavigator(navController)
            }

        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Home.route){
                HomeScreen(navHostController)
            }
            composable(Routes.Notification.route){
                NotificationScreen()
            }
            composable(Routes.Profile.route){
                ProfileScreen(navHostController)
            }
            composable(Routes.Search.route){
                SearchScreen(navHostController)
            }
            composable(Routes.AddThreads.route){
                AddThreadScreen(navController)
            }
        }
    }

}