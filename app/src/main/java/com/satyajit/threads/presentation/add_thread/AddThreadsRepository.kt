package com.satyajit.threads.presentation.add_thread

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AddThreadsRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    @ApplicationContext val context: Context,
){

    private val _newThreadsLiveData = MutableLiveData<NetworkResult<String>>()
    val newThreadsLiveData: LiveData<NetworkResult<String>>
        get() = _newThreadsLiveData

    suspend fun uploadThreads(threads: String, imageUri: Uri?){
        try{
            val time = System.currentTimeMillis()
            val image_url = imageUri?.let {
                storageReference.child(
                    "ThreadsImage/${firebaseAuth.uid + UUID.randomUUID() + time}" + getFileExtension(it,context)
                ).putFile(it).await().storage.downloadUrl.await().toString()
            }
            val threadsData = ThreadsData(
                threads,
                image_url,
                firebaseAuth.uid.toString(),
                System.currentTimeMillis().toString()
            )
            firebaseDatabase.database.getReference("Threads").child(
                firebaseDatabase.database.getReference("Threads").push().key!!
            ).setValue(threadsData).await()

            firebaseFirestore.collection("Threads").document()
                .set(threadsData).await()

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

//    suspend fun addPost(threads: String, imageUri: Uri?) {
//        try {
//            val time = System.currentTimeMillis()
//            val postLocation = firebaseAuth.uid + time
//            val fileExtension = Utils.getFileExtension(list[i], context)
//            val response = imageUri?.let {
//                storageReference.child(
//                    "Posts/${firebaseAuth.uid}/$postLocation/" + fileExtension
//                ).putFile(it).await()
//            }
//            val uriTask = response?.storage?.downloadUrl
//            while (!uriTask?.isSuccessful!!) {
//            }
//            val imageOrVideo = fileExtension?.isImageOrVideo()
//            var thumbnail = ""
//            if (imageOrVideo == 1) {
//                thumbnail = uploadFile(
//                    getThumbnailFromVideoUri(list[i], context),
//                    null,
//                    "Posts/${firebaseAuth.uid}/$postLocation/" + "item${i}Thumbnail." + fileExtension
//                ) ?: ""
//            }
//            imageOrVideo?.let {
//                PostImageVideo(
//                    it,
//                    if (it == 0) uriTask.result.toString() else "",
//                    if (it == 1) uriTask.result.toString() else "",
//                    thumbnail = thumbnail
//                )
//            }?.let { imageVideoURLList.add(it) }
//            val postData = HashMap<String, Any>()
//            postData["postId"] = postLocation
//            firebaseUser?.uid?.let { postData["creatorId"] = it }
//            postData["postDesc"] = description
//            postData["postContent"] = imageVideoURLList
//            postData["likesCount"] = 0
//            postData["commentsCount"] = 0
//            postData["likedAndCommentByMe"] = 0
//            postData["postTime"] = time
//            firebaseFireStore.collection("Posts").document(postLocation).set(postData).await()
//            _addPostStatus.postValue(NetworkResult.Success(true, 200))
//        } catch (e: Exception) {
//            Log.d("addPostException", "addPost: $e")
//            _addPostStatus.postValue(NetworkResult.Error(e.stackTraceToString(), statusCode = 500))
//        }
//    }

    private fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }


}