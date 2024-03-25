package com.satyajit.threads.presentation.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.satyajit.threads.presentation.search.SearchTopAppBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {

    val context = LocalContext.current

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
            delay(2000)
            refreshing = false
        }

    }

    Scaffold(
        topBar = {
            NotificationTopAppBar(
                onClick = {},
                scrollBehavior = scrollBehavior,
                scrollState = scrollState
            )
        }
    ) { paddingValues ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = {
                refreshing = true
                //TODO get all notifications from the viewModel
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopAppBar(
    onClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?=null,
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
                    text = "Activity",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {},
        )
    }

}
