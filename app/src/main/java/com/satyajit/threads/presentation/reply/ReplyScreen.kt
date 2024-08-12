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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.satyajit.threads.R
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.add_thread.DropDown
import com.satyajit.threads.presentation.common.BasicTextFiledWithHint
import com.satyajit.threads.presentation.common.Exoplayer
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyScreen(
    navHostController: NavHostController,
    threadData: ThreadsDataWithUserData?,
    replyViewmodel: ReplyViewmodel = hiltViewModel()
) {
    val context = LocalContext.current
    var isEnabled by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var videoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        videoUri = uri
    }

    var audioUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        audioUri = uri
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
                imageLauncher.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    "Permissions Not Granted!! Please grant permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    var visibilityText by remember {
        mutableStateOf("Anyone can reply")
    }

    var threadText by remember {
        mutableStateOf("")
    }

    val visibilityContent = listOf(
        "Anyone can reply",
        "Profiles you follow",
        "Mentioned only"
    )
    val replyPostResult by replyViewmodel.replyPostResult.observeAsState()

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(replyPostResult) {
        when (replyPostResult) {
            is NetworkResult.Error -> {
                Toast.makeText(context, replyPostResult!!.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }

            is NetworkResult.Success -> {
                if (replyPostResult!!.data != null) {
                    navHostController.navigate(Routes.Home.route) {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                isLoading = false
                navHostController.navigate(Routes.Home.route) {
                    popUpTo(navHostController.graph.id) {
                        inclusive = true
                    }
                }
            }
            null -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Reply",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentSize(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                DropDown(
                    items = visibilityContent,
                    expanded = expanded,
                    isExpanded = {
                        expanded = false
                    },
                    setText = { text ->
                        visibilityText = text
                    }
                )

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .clickable {
                            expanded = true
                        },
                    text = visibilityText,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Button(
                    onClick = {
                        replyViewmodel.postReply(
                            originalThreadId = threadData?.threads?.threadId!!,
                            replyContent = threadText,
                            imageUri = imageUri,
                            videoUri = videoUri,
                            notificationToken = threadData.user?.notificationToken!!
                        )
                        isLoading = true
                    },
                    enabled = isEnabled
                ) {
                    Text(
                        text = "Post"
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(), true)
        ) {
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
                    //post details
                    threadData?.let { data ->
                        Column(
                            modifier = Modifier.wrapContentHeight()
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                painter = if (threadData.user!!.imageUrl != "")
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
                                    .padding(horizontal = 5.dp),
                                text = data.user!!.username,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp, vertical = 5.dp),
                                text = data.threads!!.threadtxt,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            data.threads.image?.let { imageUrl ->
                                if (imageUrl.isNotEmpty()){
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
                            }
                            data.threads.video?.let { video ->
                                if(video.isNotEmpty()){
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
            //Current user details
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
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            painter = if (SharedPref.getImageUrl(context) != "")
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
                                .padding(horizontal = 5.dp),
                            text = SharedPref.getUserName(context),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
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

                        if (imageUri == null && videoUri == null && audioUri == null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 3.dp,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {

                               IconButton(onClick = {
                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        imageLauncher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest.toTypedArray())
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoLibrary,
                                        contentDescription = "photo library"
                                    )
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Outlined.GifBox,
                                        contentDescription = "gif"
                                    )
                                }
                                IconButton(onClick = {
                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        audioLauncher.launch("audio/*")
                                    } else {
                                        permissionLauncher.launch(permissionToRequest.toTypedArray())
                                    }
                                }) {

                                    Icon(
                                        imageVector = Icons.Outlined.Mic,
                                        contentDescription = "mic"
                                    )

                                }
                                IconButton(onClick = {
                                    val isGranted = permissionToRequest.all { permission ->
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }
                                    if (isGranted) {
                                        videoLauncher.launch("video/*")
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
                        } else if (videoUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .aspectRatio(1f),
                            ) {
                                Exoplayer(
                                    uri = videoUri,
                                    onRemove = {
                                        videoUri = null
                                    }
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .shadow(0.dp, shape = RoundedCornerShape(8.dp))
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

                                IconButton(
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
                            text = "Reply to ${threadData?.user?.username}",
                            style = MaterialTheme.typography.bodyMedium,
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

    if (isLoading) {
        Dialog(
            onDismissRequest = { isLoading = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color.White
                )
            }
        }
    }

}