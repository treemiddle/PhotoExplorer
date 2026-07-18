package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.core.ui.ViewEvent
import com.treemiddle.photoexplorer.core.ui.ViewSideEffect
import com.treemiddle.photoexplorer.core.ui.ViewState
import com.treemiddle.photoexplorer.domain.model.Layout
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.core.ui.UserMessage

sealed interface LikedPhotoListContract {
    data class State(
        val isLoading: Boolean = true,
        val photoList: List<LikedPhotoCard> = emptyList(),
        val isLoadingMore: Boolean = false,
        val layout: Layout = Layout.TWO_GRID
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object LoadMore : Event
        data class UnLikeClick(val photoId: String) : Event
        data object OnLayoutClick : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}