package com.treemiddle.photoexplorer.navigation

object Route {
    const val PHOTO_LIST = "photo_list"

    const val PHOTO_ID = "photoId"

    const val LIKED_PHOTO_LIST = "liked_photo_list"

    val DETAIL get() = "$PHOTO_DETAIL/{$PHOTO_ID}"

    private const val PHOTO_DETAIL = "photo_detail"

    fun detail(photoId: String): String {
        return "$PHOTO_DETAIL/$photoId"
    }
}