package com.treemiddle.photoexplorer.feature.photodetail

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState

sealed interface PhotoDetailContract {
    data class State(
        val isLiked: Boolean = false
    ) : ViewState

    sealed interface Event : ViewEvent

    sealed interface Effect : ViewSideEffect
}