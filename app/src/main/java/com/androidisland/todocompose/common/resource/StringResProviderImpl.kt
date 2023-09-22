package com.androidisland.todocompose.common.resource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StringResProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    StringResProvider {
    override fun get(input: Int): String = context.getString(input)
}