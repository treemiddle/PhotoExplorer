package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.Layout
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.feature.common.UserMessage

sealed interface LikedPhotoListContract {
    data class State(
        val isLoading: Boolean = false,
        val photoList: List<LikedPhotoCard> = emptyList(),
        val isLoadingMore: Boolean = false,
        val layout: Layout = Layout.TWO_GRID
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object LoadMore : Event
        data class UnLikeClick(val photoId: String) : Event
        data object OnClickLayout : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}