package com.treemiddle.photoexplorer.feature.photodetail

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.PhotoDetail
import com.treemiddle.photoexplorer.feature.photolist.model.UserMessage

sealed interface PhotoDetailContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isLiked: Boolean = false,
        val photoDetail: PhotoDetail? = PhotoDetail(),
        val localPhoto: LikedPhotoCard? = LikedPhotoCard()
    ) : ViewState

    sealed interface Event : ViewEvent {
        data object OnRetryClick : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}