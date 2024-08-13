package com.satyajit.threads.presentation.auth.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.User
import com.satyajit.threads.utils.NetworkResult
import com.satyajit.threads.utils.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseDatabase: DatabaseReference,
    @ApplicationContext val context: android.content.Context,
) {

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    private val _loginResultLiveData = MutableLiveData<NetworkResult<FirebaseUser>>()
    val loginResultLiveData: LiveData<NetworkResult<FirebaseUser>> get() = _loginResultLiveData

    suspend fun loginUsingEmail(email: String, password: String) {
        try {
            val response =
                firebaseAuth.signInWithEmailAndPassword(email.trim(), password.trim()).await()

            try {
                val userResponse = (firebaseAuth?.uid)?.let {
                    firebaseFirestore.collection("Users").document(
                        it
                    ).get().await()
                }
                if (userResponse?.exists() == true) {
                    val user = userResponse.toObject(User::class.java)
                    SharedPref.storeData(firebaseAuth.currentUser?.uid.toString(), user!!.name, user.email, user.bio, user.username, user.imageUrl, context)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("userDetails", "getUserData: $e")
            }

            _loginResultLiveData.postValue(NetworkResult.Success(response.user!!))
        } catch (e: Exception) {
            _loginResultLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

    private val _registerResultLiveData = MutableLiveData<NetworkResult<FirebaseUser>>()
    val registerResultLiveData: LiveData<NetworkResult<FirebaseUser>> get() = _registerResultLiveData

    suspend fun registerWithEmail(
        username: String,
        email: String,
        password: String,
    ) {
        _registerResultLiveData.postValue(NetworkResult.Loading())
        try {

            var image_url: String?="https://firebasestorage.googleapis.com/v0/b/threads-501d6.appspot.com/o/UsersImage%2Fdefault_profile_img.jpg?alt=media&token=fd2abe60-8976-49f5-b644-16b1b43507f2"
//            if(imageUri!=null){
//                image_url = imageUri?.let {
//                    storageReference.child(
//                        "UsersImage/${UUID.randomUUID()}")
//                        .putFile(it).await().storage.downloadUrl.await().toString()
//                }
//            }

            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = response.user

            SharedPref.storeData(firebaseAuth.currentUser?.uid.toString(),"",email, "", username, image_url, context)
            val newUser = User(
                "",
                user?.displayName ?: username,
                "",
                user?.email ?: "",
                user?.phoneNumber ?: "",
                image_url!!,
                "",
                emptyList(),
                ""
            )
            try {
                user?.uid?.let {
                    newUser.userId = it
                    Log.d("DATABASE_REFERENCE", firebaseDatabase.toString())
                    firebaseDatabase.child("Users").child(it).setValue(newUser).await()
                    firebaseFirestore.collection("Users").document(it).set(newUser).await()
                }

                _registerResultLiveData.postValue(NetworkResult.Success(response.user!!))

            }catch (e :Exception){
                e.printStackTrace()
                _registerResultLiveData.postValue(NetworkResult.Error(e.message.toString()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _registerResultLiveData.postValue(NetworkResult.Error(e.message.toString()))
        }
    }

    private var _userDetails = MutableLiveData<Result<User>>()
    val userDetails: LiveData<Result<User>>
        get() = _userDetails

    private var _searchUserDetails =
        MutableLiveData<Result<User?>>()
    val searchUserDetails: LiveData<Result<User?>>
        get() = _searchUserDetails

    suspend fun getUserData(user_Id: String? = null) {
        try {
            if (user_Id != null)
                _searchUserDetails.postValue(Result.success(null))
            val response = (user_Id ?: firebaseAuth?.uid)?.let {
                firebaseFirestore.collection("Users").document(
                    it
                ).get().await()
            }
            if (response?.exists() == true) {
                val otherUserList = ArrayList<String>()
                val user = response.toObject(User::class.java)
                user?.apply {
                    val userDetails = User(
                        name,
                        this.userId,
                        email,
                        phone,
                        imageUrl,
                        bio,
                    )
                    _searchUserDetails.postValue(Result.success(userDetails))
                }
            }
        } catch (e: Exception) {
            Log.d("userDetails", "getUserData: $e")
            if (user_Id == null)
                _userDetails.postValue(Result.failure(e))
            else
                _searchUserDetails.postValue(Result.failure(e))
        }
    }

    private val _updateUserDetail = MutableLiveData<NetworkResult<String>>()
    val updateUserDetail: LiveData<NetworkResult<String>>
        get() = _updateUserDetail

    suspend fun updateUserDetail(
        email: String,
        imageUri: Uri?,
        name: String,
        username: String,
        phone: String,
        bio: String,
        location: String,
        links: List<String>
    ) {
        _updateUserDetail.postValue(
            NetworkResult.Loading()
        )
        val detailMap = HashMap<String, Any>()
        try {
            var image_url: String?=SharedPref.getImageUrl(context)
            try {
                if(imageUri!=null){
                    image_url = imageUri?.let {
                        storageReference.child(
                            "UsersImage/${UUID.randomUUID()}")
                            .putFile(it).await().storage.downloadUrl.await().toString()
                    }
                }
                detailMap["imageUrl"] = image_url!!
            } catch (e: Exception) {
                _updateUserDetail.postValue(
                    NetworkResult.Error(
                        "Image Upload Failed using Default Profile Image",
                    )
                )
                Log.d("imageUploadException", "uploadNewUserDetail: $e")
                detailMap["imageUrl"] = "https://firebasestorage.googleapis.com/v0/b/threads-501d6.appspot.com/o/UsersImage%2Fdefault_profile_img.jpg?alt=media&token=fd2abe60-8976-49f5-b644-16b1b43507f2"
            }
            detailMap["email"] = firebaseAuth.currentUser!!.email.toString()
            detailMap["name"] = name
            detailMap["username"] = username
            detailMap["phone"] = phone
            detailMap["bio"] = bio
            detailMap["location"] = location
            detailMap["links"] = links
            firebaseAuth.uid?.let {
                firebaseDatabase.child("Users").child(it).updateChildren(detailMap).await()
                firebaseFirestore.collection("Users").document(it).update(detailMap).await()
                SharedPref.storeData(firebaseAuth.currentUser?.uid.toString(), name, firebaseAuth.currentUser!!.email.toString(), bio, username, image_url, context)
            }
            _updateUserDetail.postValue(
                NetworkResult.Success(
                    "Profile Updated Successfully",
                )
            )
        } catch (e: Exception) {
            Log.d("updateUser", "updateUserDetail: $e")
            _updateUserDetail.postValue(
                NetworkResult.Error(
                    "Some Error Occurred",
                )
            )
        }
    }

    private fun getFileExtension(uri: Uri, context: android.content.Context): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

}