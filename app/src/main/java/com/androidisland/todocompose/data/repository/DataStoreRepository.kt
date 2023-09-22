package com.androidisland.todocompose.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.androidisland.todocompose.enums.PrioritySort
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


@ViewModelScoped
class DataStoreRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val sortKey = stringPreferencesKey("sort")
    }

    val sortState: Flow<PrioritySort> =
        dataStore.data.catch {
            emit(emptyPreferences())
        }.map { prefs ->
            PrioritySort.valueOf(prefs[sortKey] ?: PrioritySort.DEFAULT.name)
        }

    suspend fun persistSortState(sort: PrioritySort) {
        dataStore.edit { prefs ->
            prefs[sortKey] = sort.name
        }
    }
}