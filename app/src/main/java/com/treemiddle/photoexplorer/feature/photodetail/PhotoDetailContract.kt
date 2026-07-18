package com.treemiddle.photoexplorer.feature.photodetail

import com.treemiddle.photoexplorer.core.ui.ViewEvent
import com.treemiddle.photoexplorer.core.ui.ViewSideEffect
import com.treemiddle.photoexplorer.core.ui.ViewState
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.PhotoDetail
import com.treemiddle.photoexplorer.domain.model.toLikedPhotoRequest
import com.treemiddle.photoexplorer.core.ui.UserMessage

const val PHOTO_ID_ARG = "photoId"

sealed interface PhotoDetailContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isDetailError: Boolean = false,
        val isLiked: Boolean = false,
        val photoDetail: PhotoDetail? = null,
        val localPhoto: LikedPhotoCard? = null
    ) : ViewState {
        val request = photoDetail?.toLikedPhotoRequest() ?: localPhoto?.toLikedPhotoRequest()
    }

    sealed interface Event : ViewEvent {
        data object OnRetryClick : Event
        data object OnPhotoLikeClick : Event
    }

    sealed interface Effect : ViewSideEffect {
        data class ShowMessage(val message: UserMessage) : Effect
    }
}