package com.satyajit.threads

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.satyajit.threads.navigation.Routes
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> get() =  _isLoading

    private val _startDestination = mutableStateOf(Routes.Login.route)
    val startDestination: State<String> get() = _startDestination

    init {
        viewModelScope.launch {
            if(FirebaseAuth.getInstance().currentUser!=null){
                 _startDestination.value = Routes.BottomNav.route
            }else{
                _startDestination.value = Routes.Auth.route
            }
            _isLoading.value = false
        }
    }

}