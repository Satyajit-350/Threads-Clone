package com.satyajit.threads.presentation.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.satyajit.threads.modals.Working
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.UserItem
import com.satyajit.threads.presentation.onboarding.common.ThreadWorkingItems

@Composable
fun WorkingInfo(
    navHostController: NavHostController
) {

    val working_list = listOf<Working>(
        Working(
            Icons.Outlined.SupervisedUserCircle,
            "Powered by Instagram",
            "Threads is part of the instagram platform. We will use your Threads and Instagram information to personalize ads and other experiences across Threads and Instagram. Learn More."
        ),
        Working(
            Icons.Outlined.Visibility,
            "Content Visibility",
            "If you have a public profile, anyone on or off Threads, including those in the other Meta products, can see or interact with your content."
        ),
        Working(
            Icons.Outlined.Public,
            "The fediverse",
            "Future versions of Threads will work with the fediverse, a new type of social media network that allows people to follow and interact with each others on different platforms. Learn more."
        ),
        Working(
            Icons.Outlined.Public,
            "Your Data",
            "By joining threads, you agree to the Instagram terms and conditions and acknowledge you have read the Meta Privacy policy and threads Supplemental Privacy.."
        ),
        Working(
            Icons.Outlined.SupervisedUserCircle,
            "Powered by Instagram",
            "Threads is part of the instagram platform. We will use your Threads and Instagram information to personalize ads and other experiences across Threads and Instagram. Learn More."
        ),
        Working(
            Icons.Outlined.Visibility,
            "Content Visibility",
            "If you have a public profile, anyone on or off Threads, including those in the other Meta products, can see or interact with your content."
        ),
        Working(
            Icons.Outlined.Public,
            "The fediverse",
            "Future versions of Threads will work with the fediverse, a new type of social media network that allows people to follow and interact with each others on different platforms. Learn more."
        ),
        Working(
            Icons.Outlined.Public,
            "Your Data",
            "By joining threads, you agree to the Instagram terms and conditions and acknowledge you have read the Meta Privacy policy and threads Supplemental Privacy.."
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clickable {
                            navHostController.popBackStack()
                        },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(5.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = "How Threads works",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                items(working_list) { wItems ->
                    ThreadWorkingItems(
                        title = wItems.title,
                        description = wItems.description,
                        icon = wItems.icon
                    )
                }

            }

        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 3.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                navHostController.navigate(Routes.BottomNav.route) {
                    popUpTo(navHostController.graph.id) {
                        inclusive = true
                    }
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF020202))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "Join Threads",
                fontSize = 16.sp
            )
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun WorkingInfoPreview() {
//    WorkingInfo()
//}