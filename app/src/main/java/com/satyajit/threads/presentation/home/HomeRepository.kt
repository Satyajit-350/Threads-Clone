package com.satyajit.threads.presentation.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.data.paging.ThreadsPagingSource
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import com.satyajit.threads.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
    @ApplicationContext val context: Context,
) {
    private val _threadsResultLiveData = MutableLiveData<NetworkResult<List<ThreadsDataWithUserData>>>()
    val  threadsResultLiveData: LiveData<NetworkResult<List<ThreadsDataWithUserData>>> get() = _threadsResultLiveData
    fun getThreads(): Flow<PagingData<ThreadsData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ThreadsPagingSource(
                    firebaseFireStore,
                    context
                )
            }
        ).flow
    }
    suspend fun getAllThreads(){
        _threadsResultLiveData.postValue(NetworkResult.Loading())
        try {
            val threadsList = ArrayList<ThreadsDataWithUserData>()
            val threadsResponse = firebaseFireStore
                .collection("Threads")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .get().await()
            if (!threadsResponse.isEmpty) {
                threadsResponse.documents.forEach { threadDocument ->
                    val thread = threadDocument.toObject(ThreadsData::class.java)
                    thread?.let { threadData ->
                        val userId = threadData.userId
                        val userDocument = firebaseFireStore.collection("Users").document(userId).get().await()
                        val userData = userDocument.toObject(User::class.java)
                        val threadWithUserData = ThreadsDataWithUserData(threadData, userData!!)
                        threadsList.add(threadWithUserData)
                    }
                }
                Log.d("AllThreadsOfUser", "getAllThreads: $threadsList")
                _threadsResultLiveData.postValue(NetworkResult.Success(threadsList))
            } else {
                Log.d("AllPostsOfUser", "getAllPosts: Empty")
                _threadsResultLiveData.postValue(NetworkResult.Success(threadsList))
            }
        } catch (e: Exception) {
            _threadsResultLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }
}