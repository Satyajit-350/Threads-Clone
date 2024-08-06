package com.satyajit.threads.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(){


    init {
        getAllThreads()
    }

    val getAllThreads = homeRepository.getThreads().cachedIn(viewModelScope)

    val threadsListResult = homeRepository.threadsResultLiveData

    val followOrUnFollowResult = homeRepository.followOrUnFollowResult

    val likedCountResult = homeRepository.threadLikesLiveData

    val repostCountResult = homeRepository.threadRepostLiveData

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

    fun likePost(
        threadId: String,
        isLiked: Boolean
    ){
        viewModelScope.launch(Dispatchers.IO){
            homeRepository.likePost(threadId, isLiked)
        }
    }

    fun repost(
        threadId: String,
        isReposted: Boolean
    ){
        viewModelScope.launch(Dispatchers.IO){
            homeRepository.repost(threadId, isReposted)
        }
    }

}