package com.treemiddle.photoexplorer.core.extension

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun LazyGridState.LoadMoreEffect(
    itemCount: Int,
    threshold: Int = LOAD_MORE_THRESHOLD,
    onLoadMore: () -> Unit
) {
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    LaunchedEffect(
        key1 = this,
        key2 = itemCount,
        key3 = threshold
    ) {
        snapshotFlow {
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@snapshotFlow false

            lastVisibleIndex >= itemCount - threshold
        }.collectLatest { shouldLoad ->
            if (shouldLoad) {
                currentOnLoadMore()
            }
        }
    }
}