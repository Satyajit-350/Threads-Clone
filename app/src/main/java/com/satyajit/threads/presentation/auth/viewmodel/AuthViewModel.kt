package com.satyajit.threads.presentation.auth.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyajit.threads.presentation.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val currentUser = repository.currentUser

    val loginResult = repository.loginResultLiveData

    val registerResult = repository.registerResultLiveData

    val updateUserResult = repository.updateUserDetail

    fun login(email: String, password: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.loginUsingEmail(email,password)
        }
    }

    fun register(username:String, email: String,password: String, imageUri: Uri?){
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerWithEmail(username, email, password, imageUri)
        }
    }

    fun updateDetails(
        email: String,
        username: String,
        name: String,
        imageUri: Uri?,
        phone:String,
        bio: String,
        location: String,
        links: List<String>
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserDetail(email,imageUri,name, username, phone, bio, location, links)
        }
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

}