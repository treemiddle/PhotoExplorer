package com.treemiddle.photoexplorer.data.datasource

import kotlinx.coroutines.flow.Flow

interface LayoutLocalDataSource {
    val columns: Flow<Int?>

    suspend fun update(columns: Int)
}