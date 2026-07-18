package com.treemiddle.photoexplorer.navigation

object Route {
    private const val PHOTO_DETAIL = "photo_detail"

    const val PHOTO_LIST = "photo_list"

    const val PHOTO_ID = "photoId"

    const val LIKED_PHOTO_LIST = "liked_photo_list"

    const val DETAIL = "$PHOTO_DETAIL/{$PHOTO_ID}"

    fun detail(photoId: String): String {
        return "$PHOTO_DETAIL/$photoId"
    }
}