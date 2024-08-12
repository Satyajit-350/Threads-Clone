package com.satyajit.threads.data.paging

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.lang.Error
import javax.inject.Inject

class ThreadsPagingSource constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : PagingSource<QuerySnapshot, ThreadsDataWithUserData>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, ThreadsDataWithUserData>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ThreadsDataWithUserData> =
        try {
            val currentPage =
                params.key ?: firebaseFireStore.collection("Threads")
                    .orderBy("timeStamp", Query.Direction.DESCENDING).limit(5).get().await()
            val threadsList = mutableListOf<ThreadsDataWithUserData>()

            Log.d("PagingSource", "Fetched ${currentPage.size()} items")

            for (document in currentPage.documents) {
                val threadData = document.toObject(ThreadsData::class.java)
                threadData?.let { thread ->
                    val userDocument =
                        firebaseFireStore.collection("Users").document(thread.userId).get().await()
                    val userData = userDocument.toObject(User::class.java)

                    val threadWithUserData = userData?.let {
                        ThreadsDataWithUserData(
                            threads = thread,
                            user = it,
                            isLiked = thread.likedBy.contains(firebaseAuth.currentUser?.uid),
                            isReposted = thread.repostedBy.contains(firebaseAuth.currentUser?.uid)
                        )
                    }
                    threadWithUserData?.let { threadsList.add(it) }
                }
            }

            val lastVisibleItem = currentPage.documents[currentPage.size() - 1]
            val nextPage = firebaseFireStore.collection("Threads")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .limit(5)
                    .startAfter(lastVisibleItem)
                    .get()
                    .await()

            Log.d("PagingSource", "Loaded ${threadsList.size} threads, next page exists: ${nextPage.size() > 0}")

            LoadResult.Page(
                data = threadsList,
                prevKey = null,
                nextKey = if (nextPage.isEmpty) null else nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}