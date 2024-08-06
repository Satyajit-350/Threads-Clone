package com.satyajit.threads.presentation.profile.tab_screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.satyajit.threads.presentation.common.ThreadItems
import com.satyajit.threads.presentation.profile.ProfileScreenViewModel
import com.satyajit.threads.utils.NetworkResult

@Composable
fun RepostScreen(
    navHostController: NavHostController,
    scrollState: LazyListState,
    profileViewModel: ProfileScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }

    val getRepostResult by profileViewModel.threadRepostListResult.observeAsState()

    LaunchedEffect(key1 = Unit) {
        profileViewModel.getAllRepost()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
            .background(Color.Transparent)
    ) {
        getRepostResult?.let { result ->

            isLoading = when (result) {
                is NetworkResult.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(2.dp),
                        state = scrollState
                    ) {
                        val items = result.data!!
                        item {
                            if(items.isEmpty()){
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    text = "No Repost done Yet",
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
                    false
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    false
                }

                is NetworkResult.Loading -> {
                    true
                }
            }
        }
    }
}