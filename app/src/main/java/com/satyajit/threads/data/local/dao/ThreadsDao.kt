package com.satyajit.threads.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.satyajit.threads.modals.ThreadsDataWithUserData

@Dao
interface ThreadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThreads(threads: List<ThreadsDataWithUserData>)

    @Query("SELECT * FROM threads_with_user ORDER BY timeStamp DESC")
    fun getThreads(): PagingSource<Int, ThreadsDataWithUserData>

    @Query("DELETE FROM threads_with_user")
    suspend fun clearThreads()
}