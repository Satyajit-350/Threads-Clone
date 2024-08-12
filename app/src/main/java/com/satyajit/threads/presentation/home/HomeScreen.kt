package com.satyajit.threads.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.satyajit.threads.presentation.common.ThreadItems
import com.satyajit.threads.utils.AnimUtils.handleRefreshAnimation
import com.satyajit.threads.utils.AnimUtils.rememberAnimationControllers
import com.satyajit.threads.utils.AnimUtils.rememberAnimations
import com.satyajit.threads.utils.GithubLogoPath
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val getAllThreadsResult = homeViewModel.getAllThreads.collectAsLazyPagingItems()
    var isRefreshing by remember { mutableStateOf(false) }
    val path = remember {
        Path().apply { addPath(GithubLogoPath.scaledPath.asComposePath()) }
    }
    val lines = remember {
        path.asAndroidPath().flatten(error = 0.5f).toList()
    }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        refreshThreshold = 50.dp,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                getAllThreadsResult.refresh()
            }
        }
    )
    val pullCompleted by remember {
        derivedStateOf {
            (lines.size * (pullRefreshState.progress - 0.15f)).toInt() - 10 > lines.size
        }
    }

    val (offsetYAnimation, alphaAnimation, scaleAnimation) = rememberAnimations(isRefreshing, pullRefreshState.progress)
    val (pathBackAndForthAnimationOnRefreshing, scaleAnimationOnPullCompleted) = rememberAnimationControllers()

    val hapticFeedback = LocalHapticFeedback.current
    handleRefreshAnimation(pullCompleted, scaleAnimationOnPullCompleted, hapticFeedback)
    handleLoadingState(getAllThreadsResult.loadState, isRefreshing = { refreshing ->
        isRefreshing = refreshing
    })

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pathBackAndForthAnimationOnRefreshing.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(2000), repeatMode = RepeatMode.Reverse)
            )
        } else {
            pathBackAndForthAnimationOnRefreshing.snapTo(0f)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(state = pullRefreshState)
    ){
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

@Composable
private fun handleLoadingState(
    loadState: CombinedLoadStates, isRefreshing: (Boolean) -> Unit) {
    LaunchedEffect(loadState) {
        val refreshState = loadState.refresh
        Log.d("LoadingState", "Refresh State: $refreshState")
        isRefreshing(refreshState is LoadState.Loading)
    }
}