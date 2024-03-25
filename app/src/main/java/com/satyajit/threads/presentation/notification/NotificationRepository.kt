package com.satyajit.threads.presentation.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.NotificationList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    @ApplicationContext val context: android.content.Context,
) {

    private var _notificationsList = MutableLiveData<Result<NotificationList>>()
    val notificationsList: LiveData<Result<NotificationList>>
        get() = _notificationsList

    suspend fun getNotifications() {
        try {
            firebaseAuth?.uid?.let {
                val result =
                    firebaseFireStore.collection("Notifications").document(it).get().await()
                if (result.exists()) {
                    result.toObject(NotificationList::class.java)?.let { res->
                        _notificationsList.postValue(Result.success(res))
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("getNotifications", "getNotifications: $e")
            _notificationsList.postValue(Result.failure(e))
        }
    }
}