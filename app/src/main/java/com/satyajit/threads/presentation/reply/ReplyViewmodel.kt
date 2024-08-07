package com.satyajit.threads.presentation.reply

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyViewmodel @Inject constructor(
    private val replyRepository: ReplyRepository
): ViewModel() {

    val replyPostResult = replyRepository.replyThreadsLiveData

    val replyCountResult = replyRepository.countRepliesLiveData

    fun postReply(
        originalThreadId: String,
        replyContent: String,
        imageUri: Uri?,
        videoUri: Uri?,
        notificationToken: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            replyRepository.postReply(originalThreadId,replyContent,imageUri,videoUri, notificationToken)
        }
    }

}