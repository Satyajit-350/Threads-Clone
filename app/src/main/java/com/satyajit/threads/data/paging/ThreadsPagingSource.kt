package com.satyajit.threads.data.paging

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.satyajit.threads.modals.ThreadsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.lang.Error
import javax.inject.Inject

class ThreadsPagingSource @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    @ApplicationContext val context: Context,
) : PagingSource<Int, ThreadsData>() {

    override fun getRefreshKey(state: PagingState<Int, ThreadsData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThreadsData> {
        val page = params.key ?: 1

        return try {
            val threadsList = ArrayList<ThreadsData>()
            val threadsResponse = firebaseFireStore.collection("Threads")
                .get().await()

            threadsResponse.documents.forEach {
                it.toObject(ThreadsData::class.java)?.let { threads ->
                    threadsList.add(threads)
                }
            }
            Log.d("AllThreadsOfUser", "getAllThreads: $threadsList")

            LoadResult.Page(
                data = threadsList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}