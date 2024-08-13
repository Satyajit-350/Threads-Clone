package com.satyajit.threads.connectivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.satyajit.threads.R
import com.satyajit.threads.ui.theme.green
import com.satyajit.threads.ui.theme.red
import kotlinx.coroutines.delay

@Composable
fun ConnectivityStatus(
    isConnected: Boolean
) {
    var visibility by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(isConnected = isConnected)
    }
    LaunchedEffect(isConnected) {
        if(!isConnected){
            visibility = true
        }else{
            delay(2000)
            visibility = false
        }
    }
}

@Composable
fun ConnectivityStatusBox(isConnected: Boolean) {
    val backgroundColor by animateColorAsState(targetValue = if (isConnected) green else red)
    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    id = if(isConnected) R.drawable.ic_connection_available
                    else R.drawable.ic_connection_unavailable
                ),
                contentDescription = "Connectivity Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = if(isConnected) "Back Online!" else "No Internet Connection!",
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
