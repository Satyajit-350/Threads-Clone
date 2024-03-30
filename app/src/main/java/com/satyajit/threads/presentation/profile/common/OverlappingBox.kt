package com.satyajit.threads.presentation.profile.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R

@Composable
fun OverlappingImages(
    modifier: Modifier = Modifier,
    small_circle_size: Dp,
    larger_circle_size: Dp,
    translation_val_x: Float,
    translation_val_y: Float,
    largeImage: String = "",
    smallImage: String = ""
) {

    Image(
        modifier = Modifier
            .size(small_circle_size)
            .clip(CircleShape),
        painter = if (smallImage != "")
            rememberAsyncImagePainter(
                model = smallImage
            ) else painterResource(id = R.drawable.default_profile_img),
        contentDescription = "profile_img1",
        contentScale = ContentScale.Crop
    )
    Image(
        modifier = Modifier
            .size(larger_circle_size)
            .zIndex(2f)
            .graphicsLayer {
                translationX = translation_val_x
                translationY = translation_val_y
            }
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape),
        painter = if (largeImage != "")
            rememberAsyncImagePainter(
                model = largeImage
            ) else painterResource(id = R.drawable.default_profile_img),
        contentDescription = "profile_img_2",
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun OverlappingBoxPreview() {
    OverlappingImages(
        small_circle_size = 10.dp,
        larger_circle_size = 20.dp,
        translation_val_x = -20f,
        translation_val_y = 20f
    )
}