package com.satyajit.threads.presentation.add_thread

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.Notification
import com.satyajit.threads.modals.NotificationBody
import com.satyajit.threads.modals.NotificationItem
import com.satyajit.threads.modals.NotificationList
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.User
import com.satyajit.threads.services.FCMNotificationRepository
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

class AddThreadsRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val fcmNotificationRepository: FCMNotificationRepository,
    @ApplicationContext val context: Context,
){

    private val _newThreadsLiveData = MutableLiveData<NetworkResult<String>>()
    val newThreadsLiveData: LiveData<NetworkResult<String>>
        get() = _newThreadsLiveData

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun uploadThreads(threads: String, imageUri: Uri?, videoUri: Uri?){
        try{
            val threadRef = firebaseFirestore.collection("Threads").document()
            val time = System.currentTimeMillis()
            val image_url = imageUri?.let {
                storageReference.child(
                    "ThreadsImage/${firebaseAuth.uid + UUID.randomUUID() + time}" + getFileExtension(it,context)
                ).putFile(it).await().storage.downloadUrl.await().toString()
            }
            val video_url = videoUri?.let {
                storageReference.child(
                    "ThreadsVideo/${firebaseAuth.uid + UUID.randomUUID() + time}" + getFileExtension(it,context)
                ).putFile(it).await().storage.downloadUrl.await().toString()
            }
            val threadsData = ThreadsData(
                threadId = threadRef.id,
                threads,
                image_url,
                video_url,
                firebaseAuth.uid.toString(),
                System.currentTimeMillis().toString(),
                likeCount = 0,
                likedBy = emptyList()
            )
//            firebaseDatabase.database.getReference("Threads").child(
//                firebaseDatabase.database.getReference("Threads").push().key!!
//            ).setValue(threadsData).await()

            threadRef.set(threadsData).await()

            val followers = firebaseFirestore.collection("Users")
                .document(firebaseAuth.uid.toString())
                .get()
                .await()
                .get("followers") as? List<String> ?: emptyList()

            val notificationBody = "${SharedPref.getUserName(context)} uploaded a new thread."

            followers.forEach { followerId ->
                val followerDocument =
                    firebaseFirestore.collection("Users").document(followerId).get().await()
                val followerUser = followerDocument.toObject(User::class.java)

                followerUser?.let { user ->
                    val notificationData = mapOf(
                        "type" to "THREAD_UPLOAD",
                        "profileName" to SharedPref.getUserName(context),
                        "profileImage" to SharedPref.getImageUrl(context),
                        "profileId" to firebaseAuth.uid.toString(),
                        "messageBody" to "${SharedPref.getUserName(context)} uploaded a new thread."
                    )

                    val body = Json.encodeToString(notificationData)

                    fcmNotificationRepository.sendNotification(
                        Notification(
                            token = user.notificationToken,
                            NotificationBody(
                                title = "New Thread",
                                body = body,
                            )
                        )
                    )
                    try {
                        firebaseFirestore.collection("Notifications").document(user.userId).update(
                            "notifications", FieldValue.arrayUnion(
                                NotificationItem(
                                    type = "THREAD_UPLOAD",
                                    body = body
                                )
                            )
                        ).await()
                    } catch (e: Exception) {
                        firebaseFirestore.collection("Notifications").document(user.userId).set(
                            NotificationList(
                                listOf(
                                    NotificationItem(
                                        type = "THREAD_UPLOAD",
                                        body = body
                                    )
                                )
                            )
                        ).await()
                    }
                }
            }

            _newThreadsLiveData.postValue(
                NetworkResult.Success(
                    "Threads Uploaded Successfully",
                )
            )

        }catch (e: Exception){
            e.printStackTrace()
            _newThreadsLiveData.postValue(NetworkResult.Error("Threads Upload Failed"))
        }
    }

    private fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }


}