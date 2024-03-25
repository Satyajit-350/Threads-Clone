package com.satyajit.threads.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileScreenRepository: ProfileScreenRepository
): ViewModel() {

    init {
        getThreads()
    }

    val threadsListResult = profileScreenRepository.threadsResultLiveData

    private fun getThreads(){
        viewModelScope.launch(Dispatchers.IO) {
            profileScreenRepository.getAllThreads()
        }
    }

}