package com.satyajit.threads.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.onboarding.common.OnboardingHeaderItem
import com.satyajit.threads.utils.SharedPref

@Composable
fun SaveInfo(
    navHostController:NavHostController
) {

    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0x00FFFFFF),
            darkIcons = false
        )
    }

    val gradient = Brush.linearGradient(
        0.0f to Color(0xFFFCD3E6),
        500.0f to Color(0xFFDAFBFF),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        val userId = SharedPref.getUserName(context = context)
        OnboardingHeaderItem(
            modifier = Modifier.padding(top = 40.dp),
            title = "Save your login info?",
            subTitle = "We'll save the login for ${userId}, so you won't need to enter it next time you log in."
        )

        Image(
            painter = if (SharedPref.getImageUrl(context) != "")
                rememberAsyncImagePainter(model = SharedPref.getImageUrl(context))
            else
                painterResource(id = R.drawable.default_profile_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .padding(20.dp)
                .clip(CircleShape)
                .border(8.dp, Color.White, CircleShape)
                .shadow(
                    elevation = 10.dp,
                    ambientColor = Color.Blue,
                    spotColor = Color.Cyan,
                    shape = CircleShape
                )
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                thickness = 1.dp
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    //TODO we have to save the users login info locally in datastore preferences
                    navHostController.navigate(Routes.ProfileInfo.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0274CE))
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Save",
                    fontSize = 16.sp
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                onClick = {
                    navHostController.navigate(Routes.ProfileInfo.route) {
                        popUpTo(navHostController.graph.id)
                    }
                },
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Not now",
                    fontSize = 16.sp
                )
            }
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun SaveInfoPreview() {
//    SaveInfo()
//}