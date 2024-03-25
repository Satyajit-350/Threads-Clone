package com.satyajit.threads.presentation.onboarding

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.satyajit.threads.modals.User
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.UserItem
import com.satyajit.threads.presentation.onboarding.common.CustomSearchBar
import com.satyajit.threads.presentation.onboarding.common.OnboardingHeaderItem
import com.satyajit.threads.presentation.search.SearchScreenViewModel
import com.satyajit.threads.presentation.search.common.UserSearchItem
import com.satyajit.threads.utils.NetworkResult

@Composable
fun FollowScreen(
    navHostController: NavHostController,
    searchViewModel: SearchScreenViewModel = hiltViewModel()
) {

    val getAllUsers by searchViewModel.usersListResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

    var refreshing by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    var searchText by remember {
        mutableStateOf("")
    }

    var active by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 54.dp)
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
            getAllUsers?.let { result ->
                isLoading = when (result) {
                    is NetworkResult.Success -> {
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(isRefreshing = refreshing),
                            onRefresh = {
                                refreshing = true
                                searchViewModel.getAllUsers()
                            }) {

                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(5.dp),
                                state = scrollState,
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
                                val filterItems = result.data!!.filter {
                                    it.username.contains(searchText, ignoreCase = false)
                                }
                                items(filterItems) { users ->
                                    UserSearchItem(username = users.username, name = users.name, image = users.imageUrl)
                                }
                            }
                        }
                        false
                    }

                    is NetworkResult.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        false
                    }

                    is NetworkResult.Loading -> {
                        true
                    }
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