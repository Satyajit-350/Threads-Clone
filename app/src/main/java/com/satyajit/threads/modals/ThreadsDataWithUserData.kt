package com.satyajit.threads.modals

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Entity(tableName = "threads_with_user")
data class ThreadsDataWithUserData(
    @PrimaryKey
    val id: String = "",
    val threads: ThreadsData?,
    val user: User?,
    val isLiked: Boolean = false,
    val isReposted: Boolean = false,
    val timeStamp: String = ""
): Parcelable
