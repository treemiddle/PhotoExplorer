package com.treemiddle.photoexplorer.core.ui

import androidx.annotation.StringRes
import com.treemiddle.photoexplorer.R
import com.treemiddle.photoexplorer.core.common.RateLimitException

enum class UserMessage(@param:StringRes val value: Int) {
    STORAGE_FULL(R.string.storage_full_error_text),
    LIKE_FAILED(R.string.like_failed_text),
    UNLIKE_FAILED(R.string.unlike_failed_text),
    LIKED_LIST_LOAD_FAILED(R.string.liked_photo_list_load_failed_text),
    LOAD_FAILED(R.string.full_screen_error_text),
    DETAIL_LOAD_FAILED(R.string.detail_info_error_text),
    RATE_LIMIT(R.string.rate_limit_error_text),
    NONE(-1)
}

fun Throwable.toUserMessage(message: UserMessage): UserMessage {
    return if (this is RateLimitException) {
        UserMessage.RATE_LIMIT
    } else {
        message
    }
}
