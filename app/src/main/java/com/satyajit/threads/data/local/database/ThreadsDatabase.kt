package com.satyajit.threads.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.satyajit.threads.data.local.converters.Converters
import com.satyajit.threads.data.local.dao.ThreadsDao
import com.satyajit.threads.modals.ThreadsDataWithUserData

@Database(entities = [ThreadsDataWithUserData::class], version = 2)
@TypeConverters(Converters::class)
abstract class ThreadsDatabase: RoomDatabase() {
    abstract fun threadsDao(): ThreadsDao
}