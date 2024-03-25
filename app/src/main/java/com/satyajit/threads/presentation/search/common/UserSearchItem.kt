package com.satyajit.threads.presentation.search.common

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
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun UserSearchItem(
    modifier: Modifier = Modifier,
    image: String = "",
    username: String,
    name: String,
    isFollow: Boolean = false
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    painter = if(image=="")
                        painterResource(id = R.drawable.default_profile_img)
                    else
                        rememberAsyncImagePainter(model = image),
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
                        text = username,
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
                        text = if(name!="") name else "Thread User",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp, bottom = 6.dp),
                        text = "1.4M followers",
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                if(isFollow){
                    OutlinedButton(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { /*TODO*/ }) {
                        Text(
                            text = "Follow",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }else{
                    OutlinedButton(
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { /*TODO*/ },
                    ) {
                        Text(
                            text = "Following",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

            }


            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun UserItemsPreview() {
    UserSearchItem(username = "satyajit__350", name = "Satyajit Biswal", isFollow = false)
}