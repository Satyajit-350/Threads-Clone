package com.satyajit.threads.presentation.common

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView

@SuppressLint("OpaqueUnitKey")
@Composable
fun Exoplayer(
    uri: Uri?,
    onRemove: () -> Unit,
    showController: Boolean = false,
    showRemoveBtn: Boolean = true
) {

    val context = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri(uri!!)
        .build()
    val exoPlayer = remember(context, mediaItem) {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = REPEAT_MODE_ALL
            }
    }

    DisposableEffect(
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(10.dp)),
        ){
            AndroidView(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(10.dp)),
                factory = {
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                        useController = showController
                    }
                })
            if(showRemoveBtn){
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        onRemove()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Remove image",
                        tint = Color.LightGray
                    )
                }
            }

        }
    ) {
        onDispose { exoPlayer.release() }
    }
}