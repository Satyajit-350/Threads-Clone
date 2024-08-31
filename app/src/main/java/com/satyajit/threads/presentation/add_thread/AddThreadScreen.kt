package com.satyajit.threads.presentation.add_thread

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.common.BasicTextFiledWithHint
import com.satyajit.threads.presentation.common.MediaSelector
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.PermissionManager
import com.satyajit.threads.utils.SharedPref

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AddThreadScreen(navController: NavHostController) {

    val context = LocalContext.current

    val addThreadsViewModel: AddThreadsViewModel = hiltViewModel()

    val threadsUploadResult by addThreadsViewModel.threadsUploadResult.observeAsState(null)

    var isEnabled by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    var visibilityText by remember { mutableStateOf("Anyone can reply") }

    val visibilityContent = listOf(
        "Anyone can reply",
        "Profiles you follow",
        "Mentioned only"
    )

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }

    val permissions = PermissionManager.getPermissionRequest()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> videoUri = uri }
    val audioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> audioUri = uri }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
        val allGranted = permission.all { it.value }
        if (allGranted) {
            launcher.launch("image/*")
        } else {
            Toast.makeText(context, "Permissions Not Granted!! Please grant permissions", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(threadsUploadResult) {
        when (threadsUploadResult) {
            is NetworkResult.Error -> {
                Toast.makeText(context, threadsUploadResult!!.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }

            is NetworkResult.Success -> {
                if (threadsUploadResult!!.data != null) {
                    navController.popBackStack()
                }
                isLoading = false
                navController.navigate(Routes.Home.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
            null -> {}
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

    var threadText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Header(navController = navController)

            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )

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
                            style = MaterialTheme.typography.bodyMedium
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

                        MediaSelector(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            context = context,
                            imageUri = imageUri,
                            videoUri = videoUri,
                            audioUri = audioUri,
                            onImageSelected = { imageUri = it },
                            onVideoSelected = { videoUri = it },
                            onAudioSelected = { audioUri = it },
                            permissions = permissions,
                            imageLauncher = launcher,
                            videoLauncher = videoLauncher,
                            audioLauncher = audioLauncher,
                            permissionLauncher = permissionLauncher
                        )

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.BottomCenter)
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
                    addThreadsViewModel.uploadThreads(threads = threadText, imageUri, videoUri)
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
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "clear"
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "New thread",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun DropDown(
    items: List<String>,
    expanded: Boolean,
    isExpanded: () -> Unit,
    setText: (String) -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { isExpanded() }
    ) {

        items.forEach {
            DropdownMenuItem(
                text = { Text(it) },
                onClick = {
                    setText(it)
                    isExpanded()
                }
            )
        }
    }
}
