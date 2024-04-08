package com.satyajit.threads.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.satyajit.threads.utils.SharedPref

class SharedViewModel: ViewModel() {
    var bio: MutableState<String> = mutableStateOf("")
}