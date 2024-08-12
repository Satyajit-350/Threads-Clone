package com.satyajit.threads

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.satyajit.threads.navigation.NavGraph
import com.satyajit.threads.presentation.home.HomeViewModel
import com.satyajit.threads.ui.theme.ThreadsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val homeScreenViewModel by viewModels<HomeViewModel>()
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{
            mainViewModel.isLoading.value
        }

        setContent {
            ThreadsTheme {
//                val systemUiController = rememberSystemUiController()
//
//                SideEffect {
//                    systemUiController.setStatusBarColor(
//                        color = Color(0x00FFFFFF),
//                        darkIcons = true
//                    )
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screen by mainViewModel.startDestination
                    val navController = rememberNavController()
                    NavGraph(navController = navController, startDestination = screen)
                }
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            homeScreenViewModel.updateToken(task.result)
        })
    }
}
