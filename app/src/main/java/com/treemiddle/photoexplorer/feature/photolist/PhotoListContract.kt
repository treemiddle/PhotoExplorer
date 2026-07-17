package com.treemiddle.photoexplorer.feature.photolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage

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
        data class OnPhotoLikeClick(val photoId: String) : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}