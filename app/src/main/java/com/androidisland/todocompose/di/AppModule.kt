package com.androidisland.todocompose.di

import com.androidisland.todocompose.thread.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatchers() =
        CoroutineDispatchers(
            Dispatchers.Main,
            Dispatchers.IO
        )
}