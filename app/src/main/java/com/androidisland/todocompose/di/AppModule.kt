package com.androidisland.todocompose.di

import com.androidisland.todocompose.thread.CoroutineDispatchers
import com.androidisland.todocompose.util.ActionEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


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

    @Provides
    @Singleton
    fun provideActionEventChannel() = Channel<ActionEvent?>(Channel.BUFFERED)

    @Provides
    @Singleton
    fun provideActionEventReceiver(channel: Channel<ActionEvent?>) = channel.receiveAsFlow()
}