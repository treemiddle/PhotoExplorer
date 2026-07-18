package com.treemiddle.photoexplorer.feature.photolist

import com.treemiddle.photoexplorer.core.ui.ViewEvent
import com.treemiddle.photoexplorer.core.ui.ViewSideEffect
import com.treemiddle.photoexplorer.core.ui.ViewState
import com.treemiddle.photoexplorer.domain.model.Layout
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.core.ui.UserMessage

sealed interface PhotoListContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val photoList: List<PhotoInfo> = emptyList(),
        val isLoadingMore: Boolean = false,
        val isLoadingMoreError: Boolean = false,
        val layout: Layout = Layout.TWO_GRID
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object OnRetryClick : Event
        data object LoadMore : Event
        data object RetryLoadMore : Event
        data class OnPhotoLikeClick(val photoId: String) : Event
        data object OnLayoutClick : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}