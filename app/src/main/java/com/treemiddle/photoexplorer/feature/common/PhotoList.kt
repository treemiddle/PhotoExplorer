package com.treemiddle.photoexplorer.feature.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.treemiddle.photoexplorer.common.designsystem.FooterLoading
import com.treemiddle.photoexplorer.core.extension.LoadMoreEffect

@Composable
fun <T> PhotoList(
    list: List<T>,
    key: (T) -> Any,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    isLoadingMoreError: Boolean = false,
    onRetryLoadMore: () -> Unit = {},
    item: @Composable (T) -> Unit
) {
    val lazyGirdState = rememberLazyGridState()

    lazyGirdState.LoadMoreEffect(
        itemCount = list.size,
        onLoadMore = onLoadMore
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = modifier,
        state = lazyGirdState,
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        items(
            items = list,
            key = {
                key(it)
            }
        ) {
            item(it)
        }
        if (isLoadingMore || isLoadingMoreError) {
            item(
                span = {
                    GridItemSpan(currentLineSpan = maxLineSpan)
                }
            ) {
                FooterLoading(
                    isLoading = isLoadingMore,
                    isError = isLoadingMoreError,
                    onRetry = onRetryLoadMore
                )
            }
        }
    }
}