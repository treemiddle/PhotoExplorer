package com.treemiddle.photoexplorer.feature.likedphotolist

import com.treemiddle.photoexplorer.base.ViewEvent
import com.treemiddle.photoexplorer.base.ViewSideEffect
import com.treemiddle.photoexplorer.base.ViewState

sealed interface LikedPhotoListContract {
    data class State(
        val noValue: Unit = Unit
    ) : ViewState

    sealed interface Event : ViewEvent

    sealed interface Effect : ViewSideEffect
}