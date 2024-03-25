package com.satyajit.threads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.satyajit.threads.navigation.NavGraph
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.ui.theme.ThreadsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

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
    }

}
