package com.satyajit.threads.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class ThreadsDataWithUserData(
    val threads: ThreadsData?,
    val user: User?,
    val isLiked: Boolean = false,
    val isReposted: Boolean = false
): Parcelable
