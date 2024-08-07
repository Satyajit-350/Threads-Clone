package com.satyajit.threads.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThreadsData(
    val threadId: String = "",
    val threadtxt: String = "",
    val image: String? = "",
    val video: String? = "",
    val userId: String = "",
    val timeStamp: String = "",
    val likeCount: Long = 0L,
    val repostCount: Long = 0L,
    val replyCount: Int = 0,
    val repliedBy: List<String> = emptyList(),
    val repostedBy: List<String> = emptyList(),
    val likedBy: List<String> = emptyList(),
    val replies: List<ThreadsData> = emptyList()
): Parcelable{
    val getTimeAgo: String get()  {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - timeStamp.toLong()

        val seconds = timeDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> "${years}y"
            months > 0 -> "${months}mt"
            weeks > 0 -> "${weeks}w"
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }
}
