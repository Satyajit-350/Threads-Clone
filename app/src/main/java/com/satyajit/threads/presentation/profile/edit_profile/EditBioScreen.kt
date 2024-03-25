package com.satyajit.threads.presentation.profile.edit_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.satyajit.threads.presentation.common.BasicTextFiledWithHint
import com.satyajit.threads.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBioScreen(
    navHostController: NavHostController,
) {

    var isEnabled by remember {
        mutableStateOf(false)
    }

    var bioText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

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
                            contentDescription = "close"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            SharedPref.setBio(context, bioText)
                            navHostController.popBackStack()
                        },
                        enabled = isEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "check"
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                border = BorderStroke(1.dp, Color.Gray),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Bio",
                        fontWeight = FontWeight.Bold
                    )
                    BasicTextFiledWithHint(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp, top = 5.dp),
                        hint = "Start a thread...",
                        value = bioText,
                        onValueChange = {
                            bioText = it
                            isEnabled = true
                        },
                        isEnabled = {}
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun EditBioScreenPreview() {
//    EditBioScreen()
//}