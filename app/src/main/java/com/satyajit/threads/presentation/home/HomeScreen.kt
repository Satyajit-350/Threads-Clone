package com.satyajit.threads.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.satyajit.threads.presentation.common.ListContent
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.toJson
import kotlinx.coroutines.delay
import java.net.URLEncoder

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val getAllThreadsResult by homeViewModel.threadsListResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

    var refreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(1000)
            refreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = {
            refreshing = true
            //TODO refresh the page
            homeViewModel.getAllThreads()
        }) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                getAllThreadsResult?.let { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            ListContent(
                                items = result.data!!,
                                navHostController = navHostController,
                            )
                            isLoading = false
                        }

                        is NetworkResult.Error -> {
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }

                        is NetworkResult.Loading -> {
                            isLoading = true
                        }
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}