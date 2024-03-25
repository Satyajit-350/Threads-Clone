package com.satyajit.threads.presentation.home.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ToggleIcon() {

    var isUp by remember { mutableStateOf(true) }

    IconButton(onClick = {
        isUp = !isUp
    }) {
        val icon = if (isUp) {
            Icons.Default.KeyboardArrowUp
        } else {
            Icons.Default.KeyboardArrowDown
        }

        Icon(
            imageVector = icon,
            contentDescription = if (isUp) "Up Arrow" else "Down Arrow"
        )
    }
}