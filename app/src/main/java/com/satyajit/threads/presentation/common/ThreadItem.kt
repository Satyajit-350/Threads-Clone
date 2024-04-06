package com.satyajit.threads.presentation.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.navigation.Routes

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ThreadItem(
    threadData: ThreadsDataWithUserData?,
    navHostController: NavHostController,
) {

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {

                    Column(
                        modifier = Modifier.wrapContentHeight()
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

                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .fillMaxHeight()
                                .width(1.dp)
                                .weight(1f)
                        )

                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterHorizontally)
                                .clip(CircleShape)
                                .wrapContentSize(),
                            painter = painterResource(id = R.drawable.profile_pic),
                            contentDescription = "comment_users_profile_pic",
                            contentScale = ContentScale.Crop,
                        )

                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            text = threadData?.user!!.username,
                            fontSize = 16.sp,
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp, vertical = 5.dp),
                            text = threadData.threads!!.threadtxt
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .shadow(0.dp, shape = RoundedCornerShape(8.dp))
                        ) {
                            threadData.threads.image?.let { imageUrl ->
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.8f) ,
                                    painter = rememberAsyncImagePainter(model = imageUrl),
                                    contentDescription = "thread image",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        threadData.threads.video?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .wrapContentHeight()
                                    .aspectRatio(1f),
                            ) {
                                Exoplayer(
                                    uri = threadData.threads.video.toUri(),
                                    onRemove = {},
                                    showController = true,
                                    showRemoveBtn = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Icon(
                                modifier = Modifier
                                    .clickable {  },
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "photo library"
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clickable { },
                                imageVector = Icons.Outlined.ModeComment,
                                contentDescription = "gif"
                            )
                            Icon(
                                modifier = Modifier
                                    .clickable {  },
                                imageVector = Icons.Outlined.Repeat,
                                contentDescription = "mic"
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clickable {  },
                                imageVector = Icons.Outlined.Send,
                                contentDescription = "library"
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = "5 replies . 20 likes",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Row {
                        Text(text = threadData?.threads!!.getTimeAgo)
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            modifier = Modifier
                                .clickable {
                                    //Open the bottom sheet
                                    showBottomSheet = true
                                },
                            imageVector = Icons.Rounded.MoreHoriz,
                            contentDescription = "options"
                        )
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
}

@Preview(showBackground = true)
@Composable
fun ThreadItemPreview() {
//    ThreadItem()
}