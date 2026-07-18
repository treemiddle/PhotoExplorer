package com.treemiddle.photoexplorer.local

import com.treemiddle.photoexplorer.data.datasource.LayoutLocalDataSource
import com.treemiddle.photoexplorer.local.datastore.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LayoutLocalDataSourceImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : LayoutLocalDataSource {
    override val columns: Flow<Int?> = preferencesDataStore.layoutColumns

    override suspend fun update(columns: Int) {
        preferencesDataStore.updateColumn(columns = columns)
    }
}