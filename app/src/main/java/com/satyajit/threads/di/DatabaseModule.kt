package com.satyajit.threads.di

import android.content.Context
import androidx.room.Room
import com.satyajit.threads.data.local.database.ThreadsDatabase
import com.satyajit.threads.utils.Constant.THREADS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ThreadsDatabase {
        return Room.databaseBuilder(
            context,
            ThreadsDatabase::class.java,
            THREADS_DATABASE
        ).fallbackToDestructiveMigration()
            .build()
    }

}