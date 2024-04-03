package com.satyajit.threads.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.presentation.add_thread.AddThreadScreen
import com.satyajit.threads.presentation.auth.common.AuthScreen
import com.satyajit.threads.presentation.common.BottomNavigation
import com.satyajit.threads.presentation.home.HomeScreen
import com.satyajit.threads.presentation.auth.login.LoginScreen
import com.satyajit.threads.presentation.auth.register.RegisterScreen
import com.satyajit.threads.presentation.home.thread_details.ThreadDetail
import com.satyajit.threads.presentation.notification.NotificationScreen
import com.satyajit.threads.presentation.onboarding.FollowScreen
import com.satyajit.threads.presentation.onboarding.PrivacyInfo
import com.satyajit.threads.presentation.onboarding.ProfileImport
import com.satyajit.threads.presentation.onboarding.SaveInfo
import com.satyajit.threads.presentation.onboarding.WorkingInfo
import com.satyajit.threads.presentation.profile.privacy.PrivacyScreen
import com.satyajit.threads.presentation.profile.ProfileScreen
import com.satyajit.threads.presentation.profile.edit_profile.EditBioScreen
import com.satyajit.threads.presentation.profile.edit_profile.EditProfileScreen
import com.satyajit.threads.presentation.profile.privacy.mentions.MentionsScreen
import com.satyajit.threads.presentation.search.SearchScreen
import com.satyajit.threads.presentation.profile.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.Auth.route) {
            AuthScreen(navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController)
        }
        composable(Routes.SaveInfo.route) {
            SaveInfo(navController)
        }
        composable(Routes.ProfileInfo.route) {
            ProfileImport(navController)
        }
        composable(Routes.PrivacyInfo.route) {
            PrivacyInfo(navController)
        }
        composable(Routes.FollowInfo.route) {
            FollowScreen(navController)
        }
        composable(Routes.WorkingInfo.route) {
            WorkingInfo(navController)
        }
        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.Notification.route) {
            NotificationScreen()
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Routes.Search.route) {
            SearchScreen(navController)
        }
        composable(Routes.AddThreads.route) {
            AddThreadScreen(navController)
        }
        composable(Routes.BottomNav.route) {
            BottomNavigation(navController)
        }
        composable(Routes.Settings.route,
            enterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            SettingsScreen(navController)
        }
        composable(Routes.Privacy.route,
            enterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            PrivacyScreen(navController)
        }
        composable(Routes.Mentions.route,
            enterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            MentionsScreen(navController)
        }

        composable(Routes.EditProfile.route,
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            EditProfileScreen(navHostController = navController)
        }

        composable(Routes.EditBio.route,
            enterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInVertically(
                    initialOffsetY = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            EditBioScreen(navHostController = navController)
        }

        composable(Routes.ThreadDetail.route,
            enterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                return@composable slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                return@composable slideInHorizontally(
                    initialOffsetX = { -(it) },
                    animationSpec = tween(200)
                )
            }
        ) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<ThreadsDataWithUserData?>(
                    "threads"
                )
            ThreadDetail(
                threadData = result,
                navHostController = navController
            )
        }
    }
}