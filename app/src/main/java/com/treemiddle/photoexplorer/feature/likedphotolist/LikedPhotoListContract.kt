package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage

sealed interface LikedPhotoListContract {
    data class State(
        val isLoading: Boolean = false,
        val photoList: List<LikedPhotoCard> = emptyList(),
        val isLoadingMore: Boolean = false
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object LoadMore : Event
        data class UnLikeClick(val photoId: String) : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}