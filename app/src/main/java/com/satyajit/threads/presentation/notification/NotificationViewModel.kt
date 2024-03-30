package com.satyajit.threads.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {

    init {
        getAllNotifications()
    }

    val notificationsResult = notificationRepository.notificationsList
    fun getAllNotifications(){
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepository.getNotifications()
        }
    }

}