package com.satyajit.threads.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.add_thread.AddThreadScreen
import com.satyajit.threads.presentation.home.HomeScreen
import com.satyajit.threads.presentation.notification.NotificationScreen
import com.satyajit.threads.presentation.profile.ProfileScreen
import com.satyajit.threads.presentation.search.SearchScreen

@Composable
fun BottomNavigation(navHostController: NavHostController) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { MyNavigator(navController) }
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