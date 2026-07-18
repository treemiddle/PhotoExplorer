package com.treemiddle.photoexplorer.data.repository

import com.treemiddle.photoexplorer.data.datasource.LayoutLocalDataSource
import com.treemiddle.photoexplorer.domain.model.Layout
import com.treemiddle.photoexplorer.domain.repository.LayoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LayoutRepositoryImpl @Inject constructor(
    private val localDataSource: LayoutLocalDataSource,
) : LayoutRepository {
    override val layout: Flow<Layout> = localDataSource.columns
        .map { columns ->
            if (columns == null) {
                Layout.TWO_GRID
            } else {
                Layout.toLayout(columns = columns)
            }
        }

    override suspend fun update(layout: Layout) {
        localDataSource.update(columns = layout.columns)
    }
}