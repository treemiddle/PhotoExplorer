package com.treemiddle.photoexplorer.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.treemiddle.photoexplorer.local.datastore.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataStore {
    override val layoutColumns: Flow<Int?> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[columnsKey]
        }

    private val columnsKey = intPreferencesKey(name = KEY_COLUMNS)

    override suspend fun updateColumn(columns: Int) {
        dataStore.edit {
            it[columnsKey] = columns
        }
    }

    companion object {
        const val PREFERENCES_NAME = "preferences_name"

        private const val KEY_COLUMNS = "photo_layout_columns"
    }
}