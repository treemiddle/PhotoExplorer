package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.Layout
import kotlinx.coroutines.flow.Flow

interface LayoutRepository {
    val layout: Flow<Layout>

    suspend fun update(layout: Layout)
}