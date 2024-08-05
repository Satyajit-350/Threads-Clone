package com.satyajit.threads.presentation.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.modals.User
import com.satyajit.threads.presentation.home.HomeViewModel
import com.satyajit.threads.utils.NetworkResult

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    isFollowed: MutableState<Boolean>,
    onFollowToggle: (Boolean) -> Unit,
    homeScreenViewModel: HomeViewModel = hiltViewModel()
) {

    val followUnfollowResult by homeScreenViewModel.followOrUnFollowResult.observeAsState(null)

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(followUnfollowResult) {
        when (followUnfollowResult) {
            is NetworkResult.Error -> {
                isLoading = false
                Log.d("FollowUnfollowResult", followUnfollowResult!!.message.toString())
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }

            is NetworkResult.Success -> {
                isLoading = false
            }

            null -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                painter = if (user.imageUrl != "")
                    rememberAsyncImagePainter(
                        model = user.imageUrl
                    ) else
                    painterResource(id = R.drawable.default_profile_img),
                contentDescription = "profile_pic",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = user.username,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = user.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            if (isFollowed.value) {
                OutlinedButton(
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        homeScreenViewModel.followOrUnfollow(
                            user.userId,
                            true,
                            user
                        )
                        onFollowToggle(!isFollowed.value)
                    }) {
                    Text(
                        text = "Following",
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
                Button(
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        homeScreenViewModel.followOrUnfollow(
                            user.userId,
                            false,
                            user
                        )
                        onFollowToggle(!isFollowed.value)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0274CE))
                ) {
                    Text(
                        text = "Follow",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun UserItemsPreview() {
//    UserItem(username = "satyajit__350", name = "Satyajit Biswal")
//}