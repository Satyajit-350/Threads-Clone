package com.satyajit.threads.data.local.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.satyajit.threads.modals.ThreadsData
import com.satyajit.threads.modals.User

class Converters {

    @TypeConverter
    fun fromThreadsData(threads: ThreadsData?): String? {
        return Gson().toJson(threads)
    }

    @TypeConverter
    fun toThreadsData(threadsString: String?): ThreadsData? {
        return Gson().fromJson(threadsString, object : TypeToken<ThreadsData>() {}.type)
    }

    @TypeConverter
    fun fromUser(user: User?): String? {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun toUser(userString: String?): User? {
        return Gson().fromJson(userString, object : TypeToken<User>() {}.type)
    }

}