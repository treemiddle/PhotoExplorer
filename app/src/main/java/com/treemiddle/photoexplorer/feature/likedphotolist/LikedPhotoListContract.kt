package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard

sealed interface LikedPhotoListContract {
    data class State(
        val isLoading: Boolean = false,
        val photoList: List<LikedPhotoCard> = emptyList()
    ) : ViewState

    sealed interface Event : ViewEvent

    sealed interface Effect : ViewSideEffect
}