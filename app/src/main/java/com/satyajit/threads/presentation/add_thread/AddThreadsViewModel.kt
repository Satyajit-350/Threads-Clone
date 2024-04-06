package com.satyajit.threads.presentation.add_thread

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyajit.threads.presentation.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddThreadsViewModel @Inject constructor(
    private val repository: AddThreadsRepository
) : ViewModel() {

    val threadsUploadResult = repository.newThreadsLiveData

    fun uploadThreads(threads: String, image: Uri?, video: Uri?){
        viewModelScope.launch (Dispatchers.IO){
            repository.uploadThreads(threads, image, video)
        }
    }

}