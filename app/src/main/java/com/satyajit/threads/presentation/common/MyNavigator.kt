package com.satyajit.threads.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes

@Composable
fun MyNavigator(navController: NavHostController) {

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(R.drawable.ic_home, "Home", Routes.Home.route),
            BottomNavigationItem(R.drawable.ic_search, "Search", Routes.Search.route),
            BottomNavigationItem(R.drawable.ic_add_threads, "AddThread", Routes.AddThreads.route),
            BottomNavigationItem(
                R.drawable.ic_notification,
                "Notification",
                Routes.Notification.route
            ),
            BottomNavigationItem(R.drawable.ic_person, "Profile", Routes.Profile.route),
        )
    }

    val backStackState = navController.currentBackStackEntryAsState().value


    val isBottomBarVisible  = remember(key1 = backStackState) {
        backStackState?.destination?.route == Routes.Home.route ||
                backStackState?.destination?.route == Routes.Search.route ||
                backStackState?.destination?.route == Routes.Notification.route ||
                backStackState?.destination?.route == Routes.Profile.route
    }

    if(isBottomBarVisible){
        BottomAppBar {
            bottomNavigationItems.forEach {
                val isSelected = it.route == backStackState?.destination?.route
                NavigationBarItem(selected = isSelected, onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            this.saveState = true
                        }
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(painter = painterResource(id = it.icon), contentDescription = null)
                })
            }
        }
    }
}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val text: String,
    val route: String
)