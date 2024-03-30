package com.satyajit.threads.presentation.search

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.satyajit.threads.presentation.common.UserItem
import com.satyajit.threads.presentation.onboarding.common.CustomSearchBar
import com.satyajit.threads.presentation.search.common.UserSearchItem
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    searchViewModel: SearchScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val getAllUsers by searchViewModel.usersListResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

    var active by remember {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scrollState = rememberLazyListState()

    var refreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(refreshing) {
        if(refreshing){
            delay(1000)
            refreshing = false
        }
    }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                onClick = {},
                scrollBehavior = scrollBehavior,
                scrollState = scrollState
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            var searchText by remember {
                mutableStateOf("")
            }

            CustomSearchBar(
                modifier = Modifier.padding(bottom = 10.dp),
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                active = false,
                onActiveChanged = { active = it }
            )

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
                                val filterItems = result.data!!.filter {
                                    it.username.contains(searchText, ignoreCase = false)
                                }
                                Log.d("followersLogs", SharedPref.getUserId(context)).toString()
                                items(filterItems) { users ->
                                    val isFollowedState = remember { mutableStateOf(users.followers.contains(SharedPref.getUserId(context))) }
                                    UserItem(
                                        user = users,
                                        isFollowed = isFollowedState,
                                        onFollowToggle = { newValue ->
                                            isFollowedState.value = newValue
                                        }
                                    )
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    onClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    scrollState: LazyListState
) {
    AnimatedVisibility(
        visible = scrollState.firstVisibleItemScrollOffset==0,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Search",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {},
        )
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
//    SearchScreen()
}