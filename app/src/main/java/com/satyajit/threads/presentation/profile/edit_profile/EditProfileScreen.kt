package com.satyajit.threads.presentation.profile.edit_profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.auth.viewmodel.AuthViewModel
import com.satyajit.threads.presentation.common.CustomDialog
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var isSaveEnabled by remember {
        mutableStateOf(false)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            launcher.launch("image/*")
        } else {
            Toast.makeText(
                context,
                "Permission Not Granted!! Please grant permisssion",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val updateDetailsResult by authViewModel.updateUserResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(updateDetailsResult) {
        when (updateDetailsResult) {
            is NetworkResult.Error -> {
                Toast.makeText(context, updateDetailsResult!!.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }

            is NetworkResult.Success -> {
                if (updateDetailsResult!!.data != null) {
                    navHostController.popBackStack()
                }
                isLoading = false
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

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
                                                    "Threads User",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Light,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                text = if (SharedPref.getUserName(context) != "")
                                                    SharedPref.getUserName(context)
                                                else
                                                    "Threads Username",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Light,
                                            )
                                        }
                                    }
                                }

                                Image(
                                    painter = if (imageUri == null) {
                                        if (SharedPref.getImageUrl(context) != "") {
                                            rememberAsyncImagePainter(
                                                model = SharedPref.getImageUrl(
                                                    context
                                                )
                                            )
                                        } else {
                                            painterResource(id = R.drawable.default_profile_img)
                                        }
                                    } else {
                                        rememberAsyncImagePainter(model = imageUri)
                                    },
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
                                        .clickable {
                                            val isGranted = ContextCompat.checkSelfPermission(
                                                context, permissionToRequest
                                            ) == PackageManager.PERMISSION_GRANTED
                                            if (isGranted) {
                                                launcher.launch("image/*")
                                            } else {
                                                permissionLauncher.launch(permissionToRequest)
                                            }
                                        }
                                )

                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                thickness = 1.dp
                            )
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
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
                                    text = if (SharedPref.getBio(context) != "") {
                                        SharedPref.getBio(context)
                                    } else {
                                        "+ Write bio"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                thickness = 1.dp
                            )
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        //TODO open the Add link page
                                    }
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
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                thickness = 1.dp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp)
                                        .align(Alignment.CenterVertically),
                                    text = "Private profile"
                                )

                                var isToggled by remember {
                                    mutableStateOf(false)
                                }

                                var showDialog by remember {
                                    mutableStateOf(false)
                                }

                                Switch(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .align(Alignment.CenterVertically),
                                    checked = isToggled,
                                    onCheckedChange = {
                                        isToggled = it
                                        showDialog = it
                                    }
                                )
                                CustomDialog(
                                    showDialog = showDialog,
                                    onDismiss = {
                                        showDialog = false
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.wrapContentSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            modifier = Modifier.wrapContentSize()
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 20.dp,
                                                        end = 20.dp,
                                                        top = 30.dp
                                                    ),
                                                text = "Switch to private account?",
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                fontSize = 18.sp
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .padding(
                                                        start = 10.dp,
                                                        end = 10.dp,
                                                        top = 10.dp,
                                                        bottom = 15.dp
                                                    ),
                                                text = "Only approved followers will be able to see and interact with your content.",
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center
                                            )

                                            Divider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                            )
                                            TextButton(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                onClick = {
                                                    isToggled = true
                                                    isSaveEnabled = true
                                                    showDialog = false
                                                }
                                            ) {
                                                Text(text = "Ok")
                                            }

                                            Divider(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                            )
                                            TextButton(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                onClick = {
                                                    isToggled = false
                                                    showDialog = false
                                                }
                                            ) {
                                                Text(text = "Cancel")
                                            }
                                        }
                                    }
                                }
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
                        authViewModel.updateDetails(
                            "",
                            SharedPref.getUserName(context),
                            SharedPref.getName(context),
                            imageUri!!,
                            "",
                            SharedPref.getBio(context),
                            "",
                            emptyList()
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF020202)),
                    enabled = isSaveEnabled
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = "Save",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun EditProfileScreenPreview() {
//    EditProfileScreen()
//}