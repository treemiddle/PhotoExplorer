package com.treemiddle.photoexplorer.core.ui

import androidx.annotation.StringRes
import com.treemiddle.photoexplorer.R

enum class UserMessage(@param:StringRes val value: Int) {
    STORAGE_FULL(R.string.storage_full_error_text),
    LIKE_FAILED(R.string.like_failed_text),
    UNLIKE_FAILED(R.string.unlike_failed_text),
    LIKED_LIST_LOAD_FAILED(R.string.liked_photo_list_load_failed_text)
}