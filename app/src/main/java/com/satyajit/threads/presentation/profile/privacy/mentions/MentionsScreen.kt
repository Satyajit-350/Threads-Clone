package com.satyajit.threads.presentation.profile.privacy.mentions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.satyajit.threads.presentation.common.RadioGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentionsScreen(
    navHostController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Mentions") },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(), true)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = "Allow @mentions from",
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp),
                text = "Choose who can @mention you to link your profile in their threads, replies or bio. When people try to @mention you. they'll see you don't allow @mentions.",
                color = Color.Gray,
                fontSize = 16.sp
            )
            val mentionsList = listOf(
                "Everyone",
                "Profile you follow",
                "No one"
            )
            RadioGroup(
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                radioOptions = mentionsList
            )
        }
    }
}