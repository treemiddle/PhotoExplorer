package com.treemiddle.photoexplorer.local.datastore

import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    val layoutColumns: Flow<Int?>

    suspend fun updateColumn(columns: Int)
}