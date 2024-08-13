package com.satyajit.threads.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.satyajit.threads.data.local.database.ThreadsDatabase
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.ThreadsDataWithUserData
import com.satyajit.threads.modals.User
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPagingApi::class)
class ThreadsRemoteMediator(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val threadsDatabase: ThreadsDatabase
) : RemoteMediator<Int, ThreadsDataWithUserData>() {

    private val threadsDao = threadsDatabase.threadsDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ThreadsDataWithUserData>
    ): MediatorResult {
        return try {
            val query = firebaseFireStore.collection("Threads")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(5L)
            val currentPage = when (loadType) {
                LoadType.REFRESH -> query.get().await()
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.threads?.timeStamp?.let { timeStamp ->
                        query.startAfter(timeStamp).get().await()
                    } ?: query.get().await()
                }
            }
            val threadsList = mutableListOf<ThreadsDataWithUserData>()
            for (document in currentPage.documents) {
                val threadData = document.toObject(ThreadsData::class.java)
                threadData?.let { thread ->
                    val userDocument =
                        firebaseFireStore.collection("Users").document(thread.userId).get().await()
                    val userData = userDocument.toObject(User::class.java)

                    val threadWithUserData = userData?.let {
                        ThreadsDataWithUserData(
                            id = thread.threadId,
                            threads = thread,
                            user = it,
                            isLiked = thread.likedBy.contains(firebaseAuth.currentUser?.uid),
                            isReposted = thread.repostedBy.contains(firebaseAuth.currentUser?.uid),
                            timeStamp = thread.timeStamp
                        )
                    }
                    threadWithUserData?.let { threadsList.add(it) }
                }
            }

            if (loadType == LoadType.REFRESH) {
                threadsDao.clearThreads()
            }
            threadsDao.insertThreads(threadsList)

            MediatorResult.Success(endOfPaginationReached = currentPage.documents.size < 5L)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}