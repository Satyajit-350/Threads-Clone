package com.satyajit.threads.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RowItemWithImgAndText(
    modifier: Modifier = Modifier,
    image: ImageVector,
    text: String,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(imageVector = image, contentDescription = "icon")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = text
        )
    }

}