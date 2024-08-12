package com.satyajit.threads.presentation.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.request.ImageRequest
import coil.size.Size
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.home.HomeViewModel

@Composable
fun ThreadItems(
    modifier: Modifier = Modifier,
    threadData: ThreadsDataWithUserData?,
    navHostController: NavHostController,
) {

    val context = LocalContext.current

    val viewModel: HomeViewModel = hiltViewModel()

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var showRepostSheet by remember {
        mutableStateOf(false)
    }

    var isLiked by remember {
        mutableStateOf(threadData?.isLiked)
    }

    var isReposted by remember {
        mutableStateOf(threadData?.isReposted)
    }

    val likeCount by viewModel.likedCountResult.observeAsState()
    val repostCount by viewModel.repostCountResult.observeAsState()
    val replyCount by viewModel.repliesCount.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 8.dp)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (threadData != null) {
                        navHostController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "threads",
                            value = threadData
                        )
                        navHostController.navigate(Routes.ThreadDetail.route)
                    } else {
                        Log.e("Navigation", "Thread data is null, cannot navigate")
                    }
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = if (threadData?.user?.imageUrl == "")
                        painterResource(id = R.drawable.default_profile_img)
                    else rememberAsyncImagePainter(
                        model = threadData?.user?.imageUrl
                    ),
                    contentDescription = "profile_pic",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //users data
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                text = threadData?.user!!.username,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = threadData.threads!!.getTimeAgo,
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
                                contentDescription = "options",
                            )
                        }
                        //Threads data
                        ExpandableText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            text = threadData?.threads!!.threadtxt,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        threadData.threads.image?.let { imageUrl ->
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
                        threadData.threads.video?.let { video ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .wrapContentHeight()
                                    .aspectRatio(1f),
                            ) {
                                Exoplayer(
                                    uri = video.toUri(),
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
                                icon = if (isLiked!!) {
                                    painterResource(id = R.drawable.ic_heart_filled)
                                } else {
                                    painterResource(id = R.drawable.ic_heart)
                                },
                                liked = threadData.isLiked,
                                text = if (likeCount == null) threadData.threads.likeCount.toString()
                                else likeCount.toString(),
                                onClick = {
                                    viewModel.likePost(
                                        threadId = threadData.threads.threadId,
                                        isLiked = isLiked!!
                                    )
                                    isLiked = !isLiked!!
                                },
                                changeIcon = true
                            )
                            IconText(
                                icon = painterResource(
                                    id = R.drawable.ic_message
                                ),
                                text = if (replyCount == null) threadData.threads.replyCount.toString()
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
                                icon = if (isReposted!!) {
                                    painterResource(id = R.drawable.ic_repost_once)
                                } else {
                                    painterResource(id = R.drawable.ic_repost)
                                },
                                text = if (repostCount == null) threadData.threads.repostCount.toString()
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
                }
            }
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }
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
                viewModel.repost(threadData?.threads!!.threadId, isReposted!!)
                isReposted = !isReposted!!
            },
            onRepostQuotesClicked = {
                //TODO
            },
            isReposted = isReposted!!
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun ThreadItemsPreview() {
//    ThreadItems()
//}