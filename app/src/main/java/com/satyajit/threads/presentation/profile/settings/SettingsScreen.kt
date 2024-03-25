package com.satyajit.threads.presentation.profile.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LiveHelp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.auth.viewmodel.AuthViewModel
import com.satyajit.threads.presentation.common.RowItemWithImgAndText
import com.satyajit.threads.presentation.home.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()

) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(), true)
        ) {

            RowItemWithImgAndText(
                image = Icons.Outlined.Notifications,
                text = "Notification",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.FavoriteBorder,
                text = "Notifications",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Bookmark,
                text = "Saved",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Lock,
                text = "Privacy",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Accessibility,
                text = "Accessibility",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.AccountCircle,
                text = "Account",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Translate,
                text = "Language",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.LiveHelp,
                text = "Help",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Info,
                text = "About",
                onClick = {}
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp),
                text = "Switch Profile",
                color = Color.Blue.copy(0.8f)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp)
                    .clickable {
                        authViewModel.logout()
                        navHostController.navigate(Routes.Login.route) {
                            popUpTo(navHostController.graph.startDestinationId){
                                inclusive = true
                            }
                        }
                    },
                text = "Log out",
                color = Color.Red
            )

        }

    }

}

//@Preview
//@Composable
//private fun SettingsScreenPreview() {
//    SettingsScreen()
//}