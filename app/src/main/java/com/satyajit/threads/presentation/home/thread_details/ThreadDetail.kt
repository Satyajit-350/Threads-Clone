package com.satyajit.threads.presentation.home.thread_details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadDetail(
    threadData: ThreadsDataWithUserData?,
    navHostController: NavHostController
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thread",
                    fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        //TODO
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(50.dp)
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
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .align(Alignment.CenterVertically),
                    text = "Reply",
                )
            }
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    GetThreadItem(threadData)
                }

            }
        }

    }

}

@Composable
fun GetThreadItem(
    threadData: ThreadsDataWithUserData?
) {
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
                    fontSize = 16.sp,
                )

                Text(text = data.threads!!.getTimeAgo)
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    modifier = Modifier
                        .clickable {
                            //TODO Open the bottom sheet
                        },
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = "options"
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                text = data.threads?.threadtxt ?: ""
            )

            data.threads?.image?.let { imageUrl ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "thread image",
                    contentScale = ContentScale.FillHeight
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Icon(
                        modifier = Modifier
                            .clickable { },
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
                            .clickable { },
                        imageVector = Icons.Outlined.Repeat,
                        contentDescription = "mic"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { },
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "library"
                    )
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = "5 replies . 20 likes",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun ThreadDetailsPreview() {
//    ThreadDetail()
//}
