package com.satyajit.threads.presentation.reply

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.GifBox
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Segment
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.presentation.common.BasicTextFiledWithHint
import com.satyajit.threads.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyScreen(
    navHostController: NavHostController,
    threadData: ThreadsDataWithUserData?,
) {
    val context= LocalContext.current
    var isEnabled by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionToRequest = mutableListOf<String>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        permissionToRequest.add(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        permissionToRequest.add(
            Manifest.permission.READ_MEDIA_VIDEO,
        )
        permissionToRequest.add(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionToRequest.add(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        permissionToRequest.add(
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    } else {
        permissionToRequest.add(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    "Permissions Not Granted!! Please grant permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "Reply") },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(), true)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {

                    threadData?.let { data ->
                        Column(
                            modifier = Modifier.wrapContentHeight()
                        ) {

                            Image(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                painter = if(threadData.user!!.imageUrl!="")
                                    rememberAsyncImagePainter(model = threadData.user.imageUrl)
                                else
                                    painterResource(id = R.drawable.default_profile_img),
                                contentDescription = "profile_pic",
                            )

                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                                    .fillMaxHeight()
                                    .width(1.dp)
                                    .weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    threadData?.let { data ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                text = data.user!!.username,
                                fontSize = 16.sp,
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                text = data.threads!!.threadtxt,
                            )

                            val painter = rememberAsyncImagePainter(model = data.threads.image)

                            data.threads.image?.let { imageUrl ->
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .shadow(0.dp, shape = RoundedCornerShape(8.dp))
                                        .aspectRatio(0.8f),
                                    painter = painter,
                                    contentDescription = "thread image",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    if (isEnabled) {
                        Image(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "close"
                        )
                    }
                }
            }

            var threadText by remember {
                mutableStateOf("")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp)
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
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            painter = if(SharedPref.getImageUrl(context)!="")
                                rememberAsyncImagePainter(model = SharedPref.getImageUrl(context))
                            else
                                painterResource(id = R.drawable.default_profile_img),
                            contentDescription = "profile_pic",
                        )

                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                                .fillMaxHeight()
                                .width(1.dp)
                                .weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.CenterHorizontally)
                                .clip(CircleShape)
                                .background(Color.LightGray)
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
                                .padding(horizontal = 8.dp),
                            text = SharedPref.getUserName(context),
                            fontSize = 16.sp,
                        )

                        BasicTextFiledWithHint(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            hint = "Start a thread...",
                            value = threadText,
                            onValueChange = {
                                threadText = it
                                isEnabled = true
                            },
                            isEnabled = {
                                isEnabled = it
                            }
                        )

                        if (imageUri == null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 3.dp,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {

                                androidx.compose.material3.IconButton(onClick = {

                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        launcher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest.toTypedArray())
                                    }

                                }) {

                                    Icon(
                                        imageVector = Icons.Outlined.PhotoLibrary,
                                        contentDescription = "photo library"
                                    )

                                }
                                androidx.compose.material3.IconButton(onClick = { /*TODO*/ }) {

                                    Icon(
                                        imageVector = Icons.Outlined.GifBox,
                                        contentDescription = "gif"
                                    )

                                }
                                androidx.compose.material3.IconButton(onClick = {
                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        launcher.launch("audio/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest.toTypedArray())
                                    }
                                }) {

                                    Icon(
                                        imageVector = Icons.Outlined.Mic,
                                        contentDescription = "mic"
                                    )

                                }
                                androidx.compose.material3.IconButton(onClick = {
                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        launcher.launch("video/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest.toTypedArray())
                                    }
                                }) {

                                    Icon(
                                        imageVector = Icons.Outlined.Segment,
                                        contentDescription = "library"
                                    )
                                }
                            }
                        } else {

                            val painter = rememberAsyncImagePainter(model = imageUri)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .shadow(0.dp, shape = RoundedCornerShape(8.dp))
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.8f),
                                    painter = painter,
                                    contentDescription = "thread image",
                                    contentScale = ContentScale.FillBounds
                                )

                                androidx.compose.material3.IconButton(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    onClick = {
                                        imageUri = null
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Remove image",
                                        tint = Color.LightGray
                                    )
                                }
                            }
                        }

                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = "Add to thread",
                            fontSize = 14.sp,
                            color = if (isEnabled) Color.DarkGray else Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    if (isEnabled) {
                        Image(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "close"
                        )
                    }
                }
            }
        }
    }
}