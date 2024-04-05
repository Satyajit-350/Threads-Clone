package com.satyajit.threads.presentation.auth.register

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.satyajit.threads.R
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.auth.viewmodel.AuthViewModel
import com.satyajit.threads.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val registerResult by authViewModel.registerResult.observeAsState(null)

    var isLoading by remember { mutableStateOf(false) }

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
        } else {
            Toast.makeText(
                context,
                "Permission Not Granted!! Please grant permisssion",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(registerResult) {
        when (registerResult) {
            is NetworkResult.Error -> {
                Toast.makeText(context, registerResult!!.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }

            is NetworkResult.Loading -> {
                isLoading = true
            }
            is NetworkResult.Success -> {
                if(registerResult!!.data!=null){
                    navHostController.navigate(Routes.BottomNav.route) {
                        popUpTo(navHostController.graph.id){
                            inclusive = true
                        }
                    }
                }
                isLoading = false
            }

            null -> {}
        }
    }

    val gradient = Brush.linearGradient(
        0.0f to Color(0xFFFCD3E6),
        500.0f to Color(0xFFDAFBFF),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Text(
            text = "English(US)",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 30.dp),
            fontSize = 14.sp,
            color = colorResource(id = R.color.black),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = if (imageUri == null) painterResource(id = R.drawable.instagram_logo)
            else rememberAsyncImagePainter(model = imageUri),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .align(Alignment.CenterHorizontally)
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

//        Image(
//            painter = painterResource(id = R.drawable.instagram_logo),
//            contentDescription = null,
//            modifier = Modifier
//                .width(100.dp)
//                .height(100.dp)
//                .align(Alignment.CenterHorizontally)
//        )

        Spacer(modifier = Modifier.height(60.dp))

        val usernameState = remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = usernameState.value,
            onValueChange = {
                usernameState.value = it
            },
            label = {
                Text(text = "Username")
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        val emailState = remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = emailState.value,
            onValueChange = {
                emailState.value = it
            },
            label = {
                Text(text = "Email or mobile number")
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))

        val passwordState = remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            },
            label = {
                Text(text = "Password")
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(!isLoading){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    var isValid = true

                    if (usernameState.value.isBlank()) {
                        Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (emailState.value.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please enter an email or mobile number",
                            Toast.LENGTH_SHORT
                        ).show()
                        isValid = false
                    } else if (!isValidEmail(emailState.value)) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (passwordState.value.isBlank()) {
                        Toast.makeText(context, "Please enter a password", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (isValid) {
                        authViewModel.register(
                            usernameState.value,
                            emailState.value,
                            passwordState.value,
                            imageUri
                        )
                        isLoading = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0274CE))
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Register",
                    fontSize = 16.sp
                )
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(5.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have account?",
                fontSize = 16.sp
            )
            TextButton(onClick = {
                navHostController.navigate(Routes.Login.route) {
                    popUpTo(navHostController.graph.id){
                        inclusive = true
                    }
                }
            }) {
                Text(
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.meta_black_logo),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailPattern = Regex("[a-zA-Z0–9._-]+@[a-z]+\\.+[a-z]+")
    return emailPattern.matches(email)
}

//@Preview(showBackground = true)
//@Composable
//private fun RegisterPreview() {
//    RegisterScreen()
//}