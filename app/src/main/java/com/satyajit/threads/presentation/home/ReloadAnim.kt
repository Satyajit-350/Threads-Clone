package com.satyajit.threads.presentation.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathSegment

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReloadAnim(
    modifier: Modifier = Modifier,
    offsetYAnimation: Int,
    alphaAnimation: Float,
    scaleAnimation: Float,
    scaleAnimationOnPullCompleted: Animatable<Float, AnimationVector1D>,
    path: Path,
    lines: List<PathSegment>,
    pullRefreshState: PullRefreshState,
    pathBackAndForthAnimationOnRefreshing: Animatable<Float, AnimationVector1D>,
    ) {
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(50.dp)
            .padding(end = 55.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Canvas(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(50.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    // Apply all animation values while dragging down
                    translationY = offsetYAnimation.dp.toPx()
                    alpha = alphaAnimation
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    scaleX = scaleAnimation
                    scaleY = scaleAnimation
                }
                .graphicsLayer {
                    // Scale up and down on finishing pull
                    scaleX = scaleAnimationOnPullCompleted.value
                    scaleY = scaleAnimationOnPullCompleted.value
                },
            onDraw = {
                drawPath(
                    path = path,
                    brush = SolidColor(Color.Black),
                    style = Stroke(width = 6f)
                )
            }
        )

        Canvas(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(50.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    // Apply all animation values while dragging down
                    translationY = offsetYAnimation.dp.toPx()
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    scaleX = scaleAnimation
                    scaleY = scaleAnimation
                },
            onDraw = {
                val currentLength = (lines.size * (minOf(1f, pullRefreshState.progress - 0.30f))).toInt()
                if (currentLength < 0) return@Canvas
                val minIndex = when {
                    currentLength - 10 < 0 -> 0
                    pullRefreshState.progress > 1.15f -> {
                        (lines.size * (pullRefreshState.progress - 0.15f)).toInt() - 10
                    }
                    else -> currentLength - 10
                }

                if (minIndex > lines.size) return@Canvas

                for (i in minIndex..<currentLength) {
                    drawLine(
                        brush = SolidColor(Color.White),
                        start = Offset(lines[i].start.x, lines[i].start.y),
                        end = Offset(lines[i].end.x, lines[i].end.y),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }
            }
        )

        Canvas(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(50.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    // Apply all animation values while dragging down
                    translationY = offsetYAnimation.dp.toPx()
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    scaleX = scaleAnimation
                    scaleY = scaleAnimation
                },
            onDraw = {
                val currentLength = (lines.size * pathBackAndForthAnimationOnRefreshing.value).toInt()
                val minIndex = when {
                    currentLength - 10 < 0 -> 0
                    else -> currentLength - 10
                }
                for (i in minIndex..<currentLength) {
                    drawLine(
                        brush = SolidColor(Color.White),
                        start = Offset(lines[i].start.x, lines[i].start.y),
                        end = Offset(lines[i].end.x, lines[i].end.y),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }
            }
        )
    }
}