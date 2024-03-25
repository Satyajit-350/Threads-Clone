package com.satyajit.threads.presentation.auth.common

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.view.WindowInsets.Type
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes

@Composable
fun AuthScreen(navHostController: NavHostController) {

//    val systemUiController = rememberSystemUiController()
//
//    SideEffect {
//        systemUiController.setStatusBarColor(
//            color = Color(0x00FFFFFF),
//            darkIcons = false
//        )
//    }

    val gradient = Brush.linearGradient(
        0.0f to Color(0xFFFCD3E6),
        500.0f to Color(0xFFDAFBFF),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.threads_design),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 200.dp),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillHeight,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .clickable {
                        navHostController.navigate(Routes.Login.route) {
                            popUpTo(navHostController.graph.startDestinationId){
                                inclusive = true
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Log in with Instagram",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                IconButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {}
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.instagram_logo__filled),
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .clickable {
                        navHostController.navigate(Routes.Register.route) {
                            popUpTo(navHostController.graph.startDestinationId){
                                inclusive = true
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Register with Instagram",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                IconButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {}
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.instagram_logo__filled),
                        contentDescription = null,
                    )
                }
            }
        }
    }

}
//@Preview(showBackground = true)
//@Composable
//private fun AuthScreenPreview() {
//    AuthScreen()
//}