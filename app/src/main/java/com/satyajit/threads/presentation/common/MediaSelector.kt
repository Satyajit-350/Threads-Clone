package com.satyajit.threads.presentation.common

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.GifBox
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Segment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.satyajit.threads.utils.PermissionManager

@Composable
fun MediaSelector(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    imageUri: Uri?,
    videoUri: Uri?,
    audioUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    onVideoSelected: (Uri?) -> Unit,
    onAudioSelected: (Uri?) -> Unit,
    permissions:List<String>,
    imageLauncher: ManagedActivityResultLauncher<String, Uri?>,
    videoLauncher: ManagedActivityResultLauncher<String, Uri?>,
    audioLauncher: ManagedActivityResultLauncher<String, Uri?>,
    permissionLauncher:ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {
    if (imageUri == null && videoUri == null && audioUri == null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            IconButton(onClick = {

                val isGranted = PermissionManager.checkPermissionGranted(context, permissions)
                if (isGranted) {
                    imageLauncher.launch("image/*")
                } else {
                    PermissionManager.requestPermissions(permissionLauncher, permissions)
                }

            }) {

                Icon(
                    imageVector = Icons.Outlined.PhotoLibrary,
                    contentDescription = "photo library"
                )

            }
            IconButton(onClick = { /*TODO*/ }) {

                Icon(
                    imageVector = Icons.Outlined.GifBox,
                    contentDescription = "gif"
                )

            }
            IconButton(onClick = {
                val isGranted = PermissionManager.checkPermissionGranted(context, permissions)
                if (isGranted) {
                    audioLauncher.launch("audio/*")
                } else {
                    PermissionManager.requestPermissions(permissionLauncher, permissions)
                }
            }) {

                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "mic"
                )

            }
            IconButton(onClick = {
                val isGranted = PermissionManager.checkPermissionGranted(context, permissions)
                if (isGranted) {
                    videoLauncher.launch("video/*")
                } else {
                    PermissionManager.requestPermissions(permissionLauncher, permissions)
                }
            }) {

                Icon(
                    imageVector = Icons.Outlined.Segment,
                    contentDescription = "library"
                )
            }
        }
    }
    else {
        when {
            videoUri != null -> {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .aspectRatio(1f)
                        .wrapContentHeight(),
                ) {
                    Exoplayer(
                        uri = videoUri,
                        onRemove = { onVideoSelected(null) }
                    )
                }
            }

            imageUri != null -> {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .shadow(0.dp, shape = RoundedCornerShape(8.dp))
                ) {
                    Image(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 100.dp, minWidth = 1.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(imageUri)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                        ),
                        contentDescription = "thread image",
                        contentScale = ContentScale.Fit
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = { onImageSelected(null) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Remove image",
                            tint = Color.LightGray
                        )
                    }
                }
            }

            audioUri != null -> {
                // TODO Audio URI
            }
        }
    }
}