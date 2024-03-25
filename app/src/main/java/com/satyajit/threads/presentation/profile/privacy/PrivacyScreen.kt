package com.satyajit.threads.presentation.profile.privacy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.CustomDialog
import com.satyajit.threads.presentation.common.RowItemWithImgAndText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    navHostController: NavHostController
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Privacy") },
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

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.material.IconButton(
                    onClick = {

                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = "icon")
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .align(Alignment.CenterVertically),
                    text = "Private profile"
                )

                var isToggled by remember {
                    mutableStateOf(false)
                }

                var showDialog by remember {
                    mutableStateOf(false)
                }

                Switch(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    checked = isToggled,
                    onCheckedChange = {
                        isToggled = it
                        showDialog = it
                    }
                )
                CustomDialog(
                    showDialog = showDialog,
                    onDismiss = {
                        showDialog = false
                    }
                ) {
                   Box(
                       modifier = Modifier.wrapContentSize(),
                       contentAlignment = Alignment.Center
                   ) {
                       Column(
                           modifier = Modifier.wrapContentSize()
                       ) {
                           Text(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(start = 20.dp, end = 20.dp, top = 30.dp),
                               text = "Switch to private account?",
                               fontWeight = FontWeight.Bold,
                               textAlign = TextAlign.Center,
                               fontSize = 18.sp
                           )
                           Text(
                               modifier = Modifier
                                   .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 15.dp),
                               text = "Only approved followers will be able to see and interact with your content.",
                               color = Color.Gray,
                               textAlign = TextAlign.Center
                           )

                           Divider(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                           )
                           TextButton(
                               modifier = Modifier
                                   .fillMaxWidth(),
                               onClick = {
                                   isToggled = true
                                   showDialog = false
                                   //TODO show something to indicate
                               }
                           ) {
                               Text(text = "Ok")
                           }

                           Divider(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                           )
                           TextButton(
                               modifier = Modifier
                                   .fillMaxWidth(),
                               onClick = {
                                   isToggled = false
                                   showDialog = false
                               }
                           ) {
                               Text(text = "Cancel")
                           }
                       }
                   }

                }
            }

            RowItemWithImgAndText(
                image = Icons.Outlined.Notifications,
                text = "Mentions",
                onClick = {
                    navHostController.navigate(Routes.Mentions.route)
                }
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.NotificationsOff,
                text = "Muted",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.VisibilityOff,
                text = "Hidden Words",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.Group,
                text = "Profiles you follow",
                onClick = {}
            )

            RowItemWithImgAndText(
                image = Icons.Outlined.StickyNote2,
                text = "Suggesting posts on other apps",
                onClick = {}
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Other privacy settings",
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription ="icon"
                    )
                }

                val text = buildAnnotatedString {
                    append("Some settings, like restrict, apply to both Threads and Instagram and can be managed on Instagram. ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Learn More")
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    text = text,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                androidx.compose.material.IconButton(
                    onClick = {

                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "icon")
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .align(Alignment.CenterVertically),
                    text = "Blocked"
                )

                Icon(imageVector = Icons.Outlined.Logout, contentDescription = "icon")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                androidx.compose.material.IconButton(
                    onClick = {

                    }
                ) {
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "icon")
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .align(Alignment.CenterVertically),
                    text = "Blocked"
                )

                Icon(imageVector = Icons.Outlined.Logout, contentDescription = "icon")
            }

        }
    }
}