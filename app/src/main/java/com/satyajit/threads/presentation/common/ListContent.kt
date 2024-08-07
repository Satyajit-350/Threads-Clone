package com.satyajit.threads.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.satyajit.threads.modals.ThreadsDataWithUserData

@ExperimentalCoilApi
@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    items: List<ThreadsDataWithUserData>,
    navHostController: NavHostController,
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(2.dp))
            if (items.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    text = "No Threads Posted Yet",
                    textAlign = TextAlign.Center
                )
            }
        }
        items(count = items.size) {
            items[it]?.let { threadWithUserData ->
                ThreadItems(
                    threadData = threadWithUserData,
                    navHostController = navHostController
                )
            }
        }
    }
}