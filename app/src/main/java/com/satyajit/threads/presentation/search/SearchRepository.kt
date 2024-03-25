package com.satyajit.threads.presentation.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import com.satyajit.threads.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    @ApplicationContext val context: Context,
){

    private val _usersLiveData = MutableLiveData<NetworkResult<List<User>>>()
    val usersLiveData: LiveData<NetworkResult<List<User>>>
        get() = _usersLiveData

    suspend fun getAllUsers(){
        _usersLiveData.postValue(NetworkResult.Loading())
        try {
            val usersList = ArrayList<User>()
            val response = firebaseFireStore.collection("Users").get().await()
            if (!response.isEmpty) {
                response.documents.forEach { user ->
                    val users = user.toObject(User::class.java)
                    usersList.add(users!!)
                }
                Log.d("AllUser", "getAllUsers: $usersList")
                _usersLiveData.postValue(NetworkResult.Success(usersList))
            } else {
                Log.d("AllUser", "getAllUsers: Empty")
                _usersLiveData.postValue(NetworkResult.Success(usersList))
            }
        } catch (e: Exception) {
            _usersLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }
}