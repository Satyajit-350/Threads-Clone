package com.satyajit.threads.presentation.notification.common

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.modals.NotificationItem
import com.satyajit.threads.presentation.profile.common.OverlappingImages
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun NotificationListItem(
    notificationItem: NotificationItem
) {

    val body = Json.decodeFromString<Map<String, String>>(notificationItem.body)

    //TODO i have to change the views according to the type
    if(body["type"] == "FOLLOW"){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                OverlappingImages(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    small_circle_size = 40.dp,
                    larger_circle_size = 15.dp,
                    translation_val_x = -30f,
                    translation_val_y = 70f,
                    smallImage = body["profileImage"].toString()
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
                        text = body["profileName"].toString(),
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
                        text = if(!body["messageBody"].toString().contains("unfollowed"))
                            "Followed you" else "Unfollowed you",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
//            if (isFollowed.value) {
                OutlinedButton(
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
//                        homeScreenViewModel.followOrUnfollow(
//                            user.userId,
//                            true,
//                            user
//                        )
//                        onFollowToggle(!isFollowed.value)
                    }) {
                    Text(
                        text = "Follow Back",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun NotificationItemPreview() {
//    NotificationListItem()
//}