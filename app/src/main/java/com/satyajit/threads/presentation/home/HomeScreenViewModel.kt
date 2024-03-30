package com.satyajit.threads.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.satyajit.threads.modals.User
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

    val followOrUnFollowResult = homeRepository.followOrUnFollowResult

    fun getAllThreads(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getAllThreads()
        }
    }
    fun followOrUnfollow(
        id:String,
        isFollowed: Boolean,
        user: User
    ){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.followOrUnfollowProfile(id, isFollowed, user)
        }
    }

    fun updateToken(token: String) = viewModelScope.launch(Dispatchers.IO) {
        homeRepository.updateToken(token)
    }

}