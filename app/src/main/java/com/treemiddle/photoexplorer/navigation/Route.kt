package com.treemiddle.photoexplorer.navigation

import com.treemiddle.photoexplorer.feature.photodetail.PHOTO_ID_ARG

object Route {
    private const val PHOTO_DETAIL = "photo_detail"

    const val PHOTO_LIST = "photo_list"

    const val LIKED_PHOTO_LIST = "liked_photo_list"

    const val DETAIL = "$PHOTO_DETAIL/{$PHOTO_ID_ARG}"

    fun detail(photoId: String): String {
        return "$PHOTO_DETAIL/$photoId"
    }
}
