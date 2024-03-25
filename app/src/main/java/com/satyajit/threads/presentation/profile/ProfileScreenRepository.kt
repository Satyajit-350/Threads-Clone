package com.satyajit.threads.presentation.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import com.satyajit.threads.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileScreenRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
    @ApplicationContext val context: Context,
) {

    private val _threadsResultLiveData =
        MutableLiveData<NetworkResult<List<ThreadsDataWithUserData>>>()
    val threadsResultLiveData: LiveData<NetworkResult<List<ThreadsDataWithUserData>>> get() = _threadsResultLiveData

    suspend fun getAllThreads() {

        val currentUserUid = firebaseAuth.currentUser?.uid

        _threadsResultLiveData.postValue(NetworkResult.Loading())
        try {
            val threadsList = ArrayList<ThreadsDataWithUserData>()
            val threadsResponse = firebaseFireStore
                .collection("Threads")
                .whereEqualTo("userId", currentUserUid)
                .get()
                .await()

            if (!threadsResponse.isEmpty) {
                threadsResponse.documents.forEach { threadDocument ->
                    val thread = threadDocument.toObject(ThreadsData::class.java)
                    thread?.let { threadData ->

                        val userId = threadData.userId
                        val userDocument =
                            firebaseFireStore.collection("Users").document(userId).get().await()
                        val userData = userDocument.toObject(User::class.java)

                        val threadWithUserData = ThreadsDataWithUserData(threadData, userData!!)
                        threadsList.add(threadWithUserData)
                    }
                }
                Log.d("ThreadsOfCurrentUser", "getCurrentUserThreads: $threadsList")
                _threadsResultLiveData.postValue(NetworkResult.Success(threadsList))
            } else {
                Log.d("ThreadsOfCurrentUser", "getCurrentUserThreads: Empty")
                _threadsResultLiveData.postValue(NetworkResult.Success(threadsList))
            }
        } catch (e: Exception) {
            _threadsResultLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

    suspend fun suggestedUserList(){
        //TODO
    }

}