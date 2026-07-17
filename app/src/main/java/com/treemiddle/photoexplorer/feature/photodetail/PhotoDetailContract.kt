package com.treemiddle.photoexplorer.feature.photodetail

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState
import com.treemiddle.photoexplorer.domain.model.PhotoDetail

sealed interface PhotoDetailContract {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isLiked: Boolean = false,
        val photoDetail: PhotoDetail? = null
    ) : ViewState

    sealed interface Event : ViewEvent

    sealed interface Effect : ViewSideEffect
}