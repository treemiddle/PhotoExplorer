package com.treemiddle.photoexplorer.feature.photolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.PhotoInfo

sealed interface PhotoListContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val photoList: List<PhotoInfo> = emptyList(),
        val isLoadingMore: Boolean = false,
        val isLoadingMoreError: Boolean = false
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object OnRetryClick : Event
        data object LoadMore : Event
        data object RetryLoadMore : Event
    }

    sealed interface Effect : ViewSideEffect
}