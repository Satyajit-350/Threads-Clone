package com.satyajit.threads.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    val searchRepository: SearchRepository
): ViewModel() {

    init {
        getAllUsers()
    }

    val usersListResult = searchRepository.usersLiveData

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {

          searchRepository.getAllUsers()

        }
    }
}