package com.treemiddle.photoexplorer.feature.photolist.model

import androidx.annotation.StringRes
import com.treemiddle.photoexplorer.R

enum class UserMessage(@param:StringRes val value: Int) {
    STORAGE_FULL(R.string.storage_full_error_text),
    LIKE_FAILED(R.string.like_failed_text),
    UNLIKE_FAILED(R.string.unlike_failed_text)
}