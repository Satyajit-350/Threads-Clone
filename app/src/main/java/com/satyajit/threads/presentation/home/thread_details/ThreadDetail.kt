package com.satyajit.threads.presentation.home.thread_details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.Exoplayer
import com.satyajit.threads.presentation.common.IconText
import com.satyajit.threads.presentation.common.ThreadItems
import com.satyajit.threads.presentation.common.ThreadsOptionsBottomSheet
import com.satyajit.threads.presentation.common.ThreadsRepostBottomSheet
import com.satyajit.threads.presentation.home.HomeViewModel
import com.satyajit.threads.utils.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadDetail(
    threadData: ThreadsDataWithUserData?,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val repliesResult by homeViewModel.getAllReplies.observeAsState()

    LaunchedEffect(key1 = Unit) {
        threadData?.threads?.threadId?.let { threadId ->
            homeViewModel.getAllReplies(threadId = threadId)
        }
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thread",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        //TODO
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsNone,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable {
                        if (threadData != null) {
                            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "threads",
                                value = threadData
                            )
                            navHostController.navigate(Routes.ThreadReply.route)
                        } else {
                            Log.e("Navigation", "Thread data is null, cannot navigate")
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape),
                        painter = if (threadData?.user?.imageUrl == "")
                            painterResource(id = R.drawable.default_profile_img)
                        else rememberAsyncImagePainter(
                            model = threadData?.user?.imageUrl
                        ),
                        contentDescription = "profile_pic",
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        text = "Reply to ${threadData?.user?.username}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = rememberLazyListState()
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GetThreadItem(threadData, navHostController)
                        if(isLoading){
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp)
                                    .padding(5.dp),
                                color = Color.LightGray
                            )
                        }
                    }
                }
                repliesResult?.let { result ->
                    when(result){
                        is NetworkResult.Error -> {
                            isLoading = false
                        }
                        is NetworkResult.Loading -> {
                            isLoading = true
                        }
                        is NetworkResult.Success -> {
                            Log.d("RepliesData", repliesResult?.data?.size.toString())
                            isLoading = false
                            result.data?.let { threads ->
                                Log.d("RepliesData", threads.size.toString())
                                items(count = threads.size) {
                                    threads[it]?.let { threadWithUserData ->
                                        ThreadItems(
                                            threadData = threadWithUserData,
                                            navHostController = navHostController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GetThreadItem(
    threadData: ThreadsDataWithUserData?,
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var showRepostSheet by remember {
        mutableStateOf(false)
    }

    var isLiked by remember {
        mutableStateOf(threadData!!.isLiked)
    }

    var isReposted by remember {
        mutableStateOf(threadData!!.isReposted)
    }

    val likeCount by viewModel.likedCountResult.observeAsState()
    val repostCount by viewModel.repostCountResult.observeAsState()
    val replyCount by viewModel.repliesCount.observeAsState()

    val context = LocalContext.current

    threadData?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = if (data.user?.imageUrl.isNullOrEmpty())
                        painterResource(id = R.drawable.default_profile_img)
                    else rememberAsyncImagePainter(
                        model = data.user?.imageUrl
                    ),
                    contentDescription = "profile_pic",
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    text = data.user?.username ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = data.threads!!.getTimeAgo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    modifier = Modifier
                        .clickable {
                            showBottomSheet = true
                        },
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = "options"
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                text = data.threads?.threadtxt ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            data.threads?.image?.let { imageUrl ->
                Image(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 100.dp, minWidth = 1.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                    ),
                    contentDescription = "thread image",
                    contentScale = ContentScale.Fit
                )
            }

            data.threads?.video?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .wrapContentHeight()
                        .aspectRatio(1f),
                ) {
                    Exoplayer(
                        uri = data.threads.video.toUri(),
                        onRemove = {},
                        showController = true,
                        showRemoveBtn = false
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                IconText(
                    icon = if (isLiked) {
                        painterResource(id = R.drawable.ic_heart_filled)
                    } else {
                        painterResource(id = R.drawable.ic_heart)
                    },
                    liked = threadData.isLiked,
                    text = if (likeCount == null) threadData.threads?.likeCount.toString()
                    else likeCount.toString(),
                    onClick = {
                        viewModel.likePost(
                            threadId = threadData.threads?.threadId!!,
                            isLiked = isLiked
                        )
                        isLiked = !isLiked
                    },
                    changeIcon = true
                )
                IconText(
                    icon = painterResource(
                        id = R.drawable.ic_message
                    ),
                    text = if (replyCount == null) threadData.threads?.replyCount.toString()
                    else replyCount.toString(),
                    onClick = {
                        navHostController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "threads",
                            value = threadData
                        )
                        navHostController.navigate(Routes.ThreadReply.route)
                    },
                )
                IconText(
                    icon = if (isReposted) {
                        painterResource(id = R.drawable.ic_repost_once)
                    } else {
                        painterResource(id = R.drawable.ic_repost)
                    },
                    text = if (repostCount == null) threadData.threads?.repostCount.toString()
                    else repostCount.toString(),
                    onClick = {
                        showRepostSheet = true
                    },
                )
                IconText(
                    icon = painterResource(
                        id = R.drawable.ic_share
                    ),
                    text = "",
                    onClick = {
                        //TODO
                    },
                )
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))
    }

    if (showBottomSheet) {
        ThreadsOptionsBottomSheet {
            showBottomSheet = false
        }
    }
    if (showRepostSheet) {
        ThreadsRepostBottomSheet(
            onDismiss = {
                showRepostSheet = false
            },
            onRepostClicked = {
                viewModel.repost(threadData?.threads?.threadId!!, isReposted);
                isReposted = !isReposted
            },
            onRepostQuotesClicked = {
                //TODO
            },
            isReposted = isReposted
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun ThreadDetailsPreview() {
//    ThreadDetail()
//}
