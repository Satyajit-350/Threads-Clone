package com.satyajit.threads.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.onboarding.common.OnboardingHeaderItem
import com.satyajit.threads.utils.SharedPref

@Composable
fun ProfileImport(
    navHostController: NavHostController
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clickable {
                    navHostController.popBackStack()
                },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable {
                    navHostController.navigate(Routes.PrivacyInfo.route)
                },
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null
        )

        OnboardingHeaderItem(
            modifier = Modifier.padding(top = 40.dp),
            title = "Profile",
            subTitle = "Customize your Threads profile"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.Center)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Box(modifier = Modifier.padding(10.dp)) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    text = "Name",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start
                                )
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Lock,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Column {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = if (SharedPref.getName(context) != "")
                                                SharedPref.getName(context)
                                            else
                                                "Thread User",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light,
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = if (SharedPref.getUserName(context) != "")
                                                SharedPref.getUserName(context)
                                            else
                                                "Thread_User",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light,
                                        )
                                    }
                                }
                            }

                            Image(
                                painter = if (SharedPref.getImageUrl(context) != "")
                                    rememberAsyncImagePainter(model = SharedPref.getImageUrl(context))
                                else
                                    painterResource(id = R.drawable.default_profile_img),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .shadow(
                                        elevation = 10.dp,
                                        ambientColor = Color.Blue,
                                        spotColor = Color.Cyan,
                                        shape = CircleShape
                                    )
                            )

                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            thickness = 1.dp
                        )
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                                .clickable {
                                    if (SharedPref.getBio(context) == "") {
                                        navHostController.navigate(Routes.EditBio.route)
                                    }
                                }
                        ) {
                            Text(
                                text = "Bio",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = if (SharedPref.getBio(context) != "")
                                    SharedPref.getBio(context)
                                else
                                    "+ Write bio",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            thickness = 1.dp
                        )
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Link",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "+ Add link",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                            )
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    //TODO
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF020202))
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Import from Instagram",
                    fontSize = 16.sp
                )
            }
        }

    }

}
//
//@Preview(showBackground = true)
//@Composable
//private fun profileImportPreview(){
//    ProfileImport()
//}