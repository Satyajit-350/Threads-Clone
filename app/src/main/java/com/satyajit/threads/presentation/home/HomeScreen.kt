package com.satyajit.threads.presentation.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.graphics.flatten
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.satyajit.threads.presentation.common.ListContent
import com.satyajit.threads.utils.GithubLogoPath
import com.satyajit.threads.utils.NetworkResult
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val getAllThreadsResult by homeViewModel.threadsListResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

    val path = remember {
        Path().apply { addPath(GithubLogoPath.scaledPath.asComposePath()) }
    }

    val lines = remember {
        path.asAndroidPath().flatten(error = 0.5f).toList()
    }

    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        refreshThreshold = 5.dp,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                homeViewModel.getAllThreads()
            }
        })

    // Offset Y animation of logo while dragging down
    val offsetYAnimation by animateIntAsState(targetValue = when {
        isRefreshing -> 10
        pullRefreshState.progress in 0f..1f -> (10 * pullRefreshState.progress).roundToInt()
        pullRefreshState.progress > 1f -> (10 + ((pullRefreshState.progress - 1f) * .1f) * 100).roundToInt()
        else -> 0
    })

    val alphaAnimation by animateFloatAsState(targetValue = when {
        isRefreshing -> 0.3f
        (1 - pullRefreshState.progress * 10) > 0.3f -> 1 - pullRefreshState.progress * 10
        (1 - pullRefreshState.progress * 10) < 0.3f -> 0.3f
        else -> 1f
    })

    val scaleAnimation by animateFloatAsState(targetValue = when {
        isRefreshing -> 1f
        pullRefreshState.progress + 1 > 1f -> 1f
        pullRefreshState.progress + 1 < 1f -> pullRefreshState.progress + 1
        else -> 1f
    })

    val pullCompleted by remember {
        derivedStateOf {
            (lines.size * (pullRefreshState.progress - 0.15f)).toInt() - 10 > lines.size
        }
    }

    val scaleAnimationOnPullCompleted = remember {
        Animatable(initialValue = 1f)
    }

    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(pullCompleted) {
        if (pullCompleted) {
            scaleAnimationOnPullCompleted.animateTo(1.17f)
            scaleAnimationOnPullCompleted.animateTo(1f)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val pathBackAndForthAnimationOnRefreshing = remember {
        Animatable(initialValue = 0f)
    }

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

    Box(
        modifier = Modifier.pullRefresh(state = pullRefreshState)
    ) {

        ReloadAnim(
            modifier = Modifier.align(Alignment.TopCenter),
            offsetYAnimation = offsetYAnimation,
            alphaAnimation = alphaAnimation,
            scaleAnimation = scaleAnimation,
            scaleAnimationOnPullCompleted = scaleAnimationOnPullCompleted,
            path = path,
            lines = lines,
            pullRefreshState = pullRefreshState,
            pathBackAndForthAnimationOnRefreshing = pathBackAndForthAnimationOnRefreshing
        )

        Box(modifier = Modifier.fillMaxSize()
            .padding(top = 60.dp)){
            getAllThreadsResult?.let { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        isRefreshing = false
                        ListContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    translationY = offsetYAnimation.dp.toPx()
                                },
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
                        isRefreshing = true
                        isLoading = true
                    }
                }
            }
        }
    }
}