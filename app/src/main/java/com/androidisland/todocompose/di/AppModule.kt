package com.androidisland.todocompose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.androidisland.todocompose.common.resource.StringResProvider
import com.androidisland.todocompose.common.resource.StringResProviderImpl
import com.androidisland.todocompose.data.models.TaskActionEvent
import com.androidisland.todocompose.ext.dataStore
import com.androidisland.todocompose.thread.CoroutineDispatchers
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.dataStore

        @Provides
        @Singleton
        fun provideCoroutineDispatchers() =
            CoroutineDispatchers(
                Dispatchers.Main,
                Dispatchers.IO
            )

        @Provides
        @Singleton
        fun provideTaskActionEventChannel() =
            Channel<TaskActionEvent>(Channel.BUFFERED)

        @Provides
        @Singleton
        fun provideTaskActionEventReceiver(channel: Channel<TaskActionEvent>) =
            channel.receiveAsFlow()

    }

    @Binds
    @Singleton
    abstract fun bindStringResProvider(provider: StringResProviderImpl): StringResProvider
}