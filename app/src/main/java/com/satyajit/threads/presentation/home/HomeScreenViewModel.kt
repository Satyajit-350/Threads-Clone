package com.satyajit.threads.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(){

    init {
        getAllThreads()
    }

    val getAllThreads = homeRepository.getThreads().cachedIn(viewModelScope)

    val threadsListResult = homeRepository.threadsResultLiveData

    fun getAllThreads(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getAllThreads()
        }
    }

}