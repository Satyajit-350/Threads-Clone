package com.satyajit.threads.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlin.math.roundToInt

object AnimUtils {

    @Composable
    fun rememberAnimations(isRefreshing: Boolean, pullRefreshProgress: Float): Triple<Int, Float, Float> {
        val offsetYAnimation by animateIntAsState(targetValue = when {
            isRefreshing -> 10
            pullRefreshProgress in 0f..1f -> (10 * pullRefreshProgress).roundToInt()
            pullRefreshProgress > 1f -> (10 + ((pullRefreshProgress - 1f) * .1f) * 100).roundToInt()
            else -> 0
        })

        val alphaAnimation by animateFloatAsState(targetValue = when {
            isRefreshing -> 0.3f
            (1 - pullRefreshProgress * 10) > 0.3f -> 1 - pullRefreshProgress * 10
            (1 - pullRefreshProgress * 10) < 0.3f -> 0.3f
            else -> 1f
        }, label = ""
        )

        val scaleAnimation by animateFloatAsState(targetValue = when {
            isRefreshing -> 1f
            pullRefreshProgress + 1 > 1f -> 1f
            pullRefreshProgress + 1 < 1f -> pullRefreshProgress + 1
            else -> 1f
        }, label = ""
        )
        return Triple(offsetYAnimation, alphaAnimation, scaleAnimation)
    }

    @Composable
    fun rememberAnimationControllers(): Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>> {
        return remember {
            Pair(
                Animatable(initialValue = 0f),
                Animatable(initialValue = 1f)
            )
        }
    }

    @Composable
    fun handleRefreshAnimation(
        pullCompleted: Boolean,
        scaleAnimationOnPullCompleted: Animatable<Float, *>,
        hapticFeedback: HapticFeedback
    ) {

        LaunchedEffect(pullCompleted) {
            if (pullCompleted) {
                scaleAnimationOnPullCompleted.animateTo(1.17f)
                scaleAnimationOnPullCompleted.animateTo(1f)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
}