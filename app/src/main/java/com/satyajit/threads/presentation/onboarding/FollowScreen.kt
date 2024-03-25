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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.satyajit.threads.modals.User
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.UserItem
import com.satyajit.threads.presentation.onboarding.common.CustomSearchBar
import com.satyajit.threads.presentation.onboarding.common.OnboardingHeaderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    navHostController: NavHostController
) {

    val users_list = listOf<User>(
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
        User(name = "Satyajit", username = "satya_350"),
    )

    var searchText by remember {
        mutableStateOf("")
    }

    var active by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
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

                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clickable {
                            navHostController.navigate(Routes.WorkingInfo.route)
                        },
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )

            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(5.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        OnboardingHeaderItem(
                            title = "Follow the same accounts you follow on Instagram.",
                            subTitle = "How it Works"
                        )
                        CustomSearchBar(
                            modifier = Modifier.padding(bottom = 10.dp),
                            searchText = searchText,
                            onSearchTextChanged = { searchText = it },
                            active = false,
                            onActiveChanged = { active = it }
                        )
                    }
                }
                items(users_list) { users ->
                    UserItem(username = users.username, name = users.name)
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 3.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                //TODO follow all users give a loader and then move to next page
                navHostController.navigate(Routes.WorkingInfo.route)
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF020202))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "Follow all",
                fontSize = 16.sp
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//private fun FollowScreenPreview() {
//    FollowScreen()
//}