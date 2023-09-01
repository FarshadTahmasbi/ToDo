package com.androidisland.todocompose.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.androidisland.todocompose.util.Constants.PREFERENCES_NAME


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)
