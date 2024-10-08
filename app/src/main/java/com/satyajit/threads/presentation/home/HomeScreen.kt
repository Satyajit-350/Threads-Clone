package com.satyajit.threads.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.graphics.flatten
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.satyajit.threads.presentation.common.ThreadItems
import com.satyajit.threads.utils.AnimUtils.handleRefreshAnimation
import com.satyajit.threads.utils.AnimUtils.rememberAnimationControllers
import com.satyajit.threads.utils.AnimUtils.rememberAnimations
import com.satyajit.threads.utils.GithubLogoPath

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val getAllThreadsResult = homeViewModel.getAllThreads.collectAsLazyPagingItems()
    val path = remember {
        Path().apply { addPath(GithubLogoPath.scaledPath.asComposePath()) }
    }
    val lines = remember {
        path.asAndroidPath().flatten(error = 0.5f).toList()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = getAllThreadsResult.loadState.refresh is LoadState.Loading,
        refreshThreshold = 50.dp,
        onRefresh = {
            getAllThreadsResult.refresh()
        }
    )
    val pullCompleted by remember {
        derivedStateOf {
            (lines.size * (pullRefreshState.progress - 0.15f)).toInt() - 10 > lines.size
        }
    }

    val (offsetYAnimation, alphaAnimation, scaleAnimation) = rememberAnimations(getAllThreadsResult.loadState.refresh is LoadState.Loading, pullRefreshState.progress)
    val (pathBackAndForthAnimationOnRefreshing, scaleAnimationOnPullCompleted) = rememberAnimationControllers()

    val hapticFeedback = LocalHapticFeedback.current
    handleRefreshAnimation(pullCompleted, scaleAnimationOnPullCompleted, hapticFeedback)

    LaunchedEffect(getAllThreadsResult.loadState.refresh) {
        val refreshState = getAllThreadsResult.loadState.refresh
        if (refreshState is LoadState.Loading) {
            pathBackAndForthAnimationOnRefreshing.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(2000), repeatMode = RepeatMode.Reverse)
            )
        } else if (refreshState is LoadState.NotLoading) {
            // Refresh complete, reset isRefreshing
            pathBackAndForthAnimationOnRefreshing.snapTo(0f)
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(state = pullRefreshState)
    ){
        Column {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = offsetYAnimation.dp.toPx()
                    },
                contentPadding = PaddingValues(2.dp),
            ) {
                item {
                    ReloadAnim(
                        modifier = Modifier.fillMaxWidth(),
                        offsetYAnimation = offsetYAnimation,
                        alphaAnimation = alphaAnimation,
                        scaleAnimation = scaleAnimation,
                        scaleAnimationOnPullCompleted = scaleAnimationOnPullCompleted,
                        path = path,
                        lines = lines,
                        pullRefreshState = pullRefreshState,
                        pathBackAndForthAnimationOnRefreshing = pathBackAndForthAnimationOnRefreshing,
                    )
                }
                items(count = getAllThreadsResult.itemCount) {
                    getAllThreadsResult[it]?.let { threadWithUserData ->
                        ThreadItems(
                            threadData = threadWithUserData,
                            navHostController = navHostController
                        )
                    }
                }
            }

        }

    }
}