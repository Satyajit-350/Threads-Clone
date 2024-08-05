package com.satyajit.threads.presentation.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.data.paging.ThreadsPagingSource
import com.satyajit.threads.modals.Notification
import com.satyajit.threads.modals.NotificationBody
import com.satyajit.threads.modals.NotificationItem
import com.satyajit.threads.modals.NotificationList
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import com.satyajit.threads.services.FCMNotificationRepository
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class HomeRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    private val fcmNotificationRepository: FCMNotificationRepository,
    @ApplicationContext val context: Context,
) {
    private val _threadsResultLiveData =
        MutableLiveData<NetworkResult<List<ThreadsDataWithUserData>>>()
    val threadsResultLiveData: LiveData<NetworkResult<List<ThreadsDataWithUserData>>> get() = _threadsResultLiveData
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

    suspend fun getAllThreads() {
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
                        val userDocument =
                            firebaseFireStore.collection("Users").document(userId).get().await()
                        val userData = userDocument.toObject(User::class.java)
                        val threadWithUserData = ThreadsDataWithUserData(
                            threadData,
                            userData!!,
                            isLiked = threadData.likedBy.contains(firebaseAuth.currentUser?.uid)
                        )
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

    private var _followOrUnFollowResult = MutableLiveData<NetworkResult<Boolean>>()
    val followOrUnFollowResult: LiveData<NetworkResult<Boolean>>
        get() = _followOrUnFollowResult
    suspend fun followOrUnfollowProfile(
        id: String,
        isFollowed: Boolean,
        user: User
    ) = coroutineScope {
        try {
            firebaseAuth.currentUser?.uid?.let {
                val body = Json.encodeToString(
                    mapOf(
                        Pair("type", "FOLLOW"),
                        Pair("profileName", SharedPref.getUserName(context)),
                        Pair("profileImage", SharedPref.getImageUrl(context)),
                        Pair("profileId", firebaseAuth.currentUser?.uid.toString()),
                        Pair("messageBody", if (!isFollowed) "${SharedPref.getUserName(context)} started following you"
                        else "${SharedPref.getUserName(context)} unfollowed you")
                    )
                )
                launch {
                    firebaseFireStore.collection("Users").document(it).update(
                        "following",
                        if (isFollowed){
                            FieldValue.arrayRemove(id)
                        } else {
                            FieldValue.arrayUnion(id)
                        }
                    ).await()
                    firebaseFireStore.collection("Users").document(user.userId).update(
                        "followers",
                        if(isFollowed){
                            FieldValue.arrayRemove(firebaseAuth.currentUser?.uid.toString())
                        }else{
                            FieldValue.arrayUnion(firebaseAuth.currentUser?.uid.toString())
                        }
                    ).await()
                    fcmNotificationRepository.sendNotification(
                        Notification(
                            token = user.notificationToken,
                            NotificationBody(
                                title = "Follow",
                                body = body,
                            )
                        )
                    )
                }
                try {
                    firebaseFireStore.collection("Notifications").document(user.userId).update(
                        "notifications", FieldValue.arrayUnion(
                            NotificationItem(
                                type = "FOLLOW",
                                body = body
                            )
                        )
                    ).await()
                } catch (e: Exception) {
                    firebaseFireStore.collection("Notifications").document(user.userId).set(
                        NotificationList(
                            listOf(
                                NotificationItem(
                                    type = "FOLLOW",
                                    body = body
                                )
                            )
                        )
                    ).await()
                }
                _followOrUnFollowResult.postValue(NetworkResult.Success(true))
            }
        } catch (e: Exception) {
            Log.d("followProfile", "followProfile: ")
            _followOrUnFollowResult.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

    private val _threadLikesLiveData = MutableLiveData<String>()
    val threadLikesLiveData: LiveData<String> get() = _threadLikesLiveData

    suspend fun likePost(
        threadId: String,
        isLiked: Boolean
    ){
        try {
            firebaseAuth.currentUser?.uid.let { userId ->
                firebaseFireStore.runTransaction { transaction ->
                    val threadRef = firebaseFireStore.collection("Threads").document(threadId)
                    val snapshot = transaction.get(threadRef)

                    val currentLikeCount = snapshot.getLong("likeCount") ?: 0L
                    val newLikeCount = if(isLiked) currentLikeCount - 1 else currentLikeCount + 1

                    transaction.update(threadRef, "likeCount", newLikeCount)
                    _threadLikesLiveData.postValue(
                        newLikeCount.toString()
                    )
                    if(isLiked){
                        transaction.update(threadRef, "likedBy", FieldValue.arrayRemove(userId))
                    }else{
                        transaction.update(threadRef, "likedBy", FieldValue.arrayUnion(userId))
                    }
                }.await()
            }
            Log.d("Like Post:", "Like Successful")
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("Like Post:", e.localizedMessage!!)
        }
    }

    suspend fun updateToken(token: String) {
        try {
            firebaseAuth.currentUser?.uid?.let {
                firebaseFireStore.collection("Users").document(it)
                    .set(mapOf(Pair("notificationToken", token)), SetOptions.merge()).await()
            }
        } catch (e: Exception) {
            Log.d("saveToken", "updateToken: $e")
        }
    }
}