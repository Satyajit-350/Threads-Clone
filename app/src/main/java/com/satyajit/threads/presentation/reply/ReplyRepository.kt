package com.satyajit.threads.presentation.reply

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.Notification
import com.satyajit.threads.modals.NotificationBody
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.services.FCMNotificationRepository
import com.satyajit.threads.utils.Constant
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class ReplyRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    private val fcmNotificationRepository: FCMNotificationRepository,
    @ApplicationContext val context: Context,
) {

    private val _replyThreadsLiveData = MutableLiveData<NetworkResult<String>>()
    val replyThreadsLiveData: LiveData<NetworkResult<String>>
        get() = _replyThreadsLiveData

    private val _countRepliesLiveData = MutableLiveData<String>()
    val countRepliesLiveData: LiveData<String> get() = _countRepliesLiveData

    suspend fun postReply(
        originalThreadId: String,
        replyContent: String,
        imageUri: Uri?,
        videoUri: Uri?,
        notificationToken: String
    ){
        _replyThreadsLiveData.postValue(NetworkResult.Loading())
        try {
            val mainRef = firebaseFirestore.collection("Threads").document(originalThreadId)
            val replyRef = mainRef.collection("Replies").document()

            val time = System.currentTimeMillis()

            val image_url = imageUri?.let {
                storageReference.child(
                    "ThreadsImage/${firebaseAuth.uid + UUID.randomUUID() + time}" + Constant.getFileExtension(it, context)
                ).putFile(it).await().storage.downloadUrl.await().toString()
            }
            val video_url = videoUri?.let {
                storageReference.child(
                    "ThreadsVideo/${firebaseAuth.uid + UUID.randomUUID() + time}" + Constant.getFileExtension(it, context)
                ).putFile(it).await().storage.downloadUrl.await().toString()
            }

            val replyData = ThreadsData(
                threadId = replyRef.id,
                threadtxt = replyContent,
                image = image_url,
                video = video_url,
                userId = firebaseAuth.currentUser?.uid.toString(),
                timeStamp = time.toString()
            )

            replyRef.set(replyData).await()

            firebaseFirestore.runTransaction { transaction ->
                val snapshot = transaction.get(mainRef)
                val currentReplyCount = snapshot.getLong("replyCount") ?: 0L
                val newReplyCount = currentReplyCount + 1

                transaction.update(mainRef, "replyCount", newReplyCount)
                transaction.update(mainRef, "repliedBy", FieldValue.arrayUnion(firebaseAuth.currentUser?.uid))

                _countRepliesLiveData.postValue(
                    newReplyCount.toString()
                )
            }.await()

            val notificationData = mapOf(
                "type" to "THREAD_REPLY",
                "profileName" to SharedPref.getUserName(context),
                "profileImage" to SharedPref.getImageUrl(context),
                "profileId" to firebaseAuth.currentUser?.uid.toString(),
                "messageBody" to  "${SharedPref.getUserName(context)} replied to your thread."
            )

            val body = Json.encodeToString(notificationData)

            fcmNotificationRepository.sendNotification(
                Notification(
                    token = notificationToken,
                    NotificationBody(
                        title = "Reply",
                        body = body,
                    )
                )
            )
            _replyThreadsLiveData.postValue(
                NetworkResult.Success("Reply Uploaded Successfully")
            )
        }catch (e: Exception){
            e.printStackTrace()
            _replyThreadsLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }
}