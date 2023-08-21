package com.androidisland.todocompose.di

import android.content.Context
import androidx.room.Room
import com.androidisland.todocompose.data.ToDoDao
import com.androidisland.todocompose.data.ToDoDatabase
import com.androidisland.todocompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ToDoDatabase =
        Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideToDoDao(database: ToDoDatabase): ToDoDao = database.toDoDao()

}