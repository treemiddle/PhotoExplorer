package com.treemiddle.photoexplorer.data.model

data class LikedPhotoData(
    val id: String = "",
    val description: String = "",
    val authorName: String = "",
    val authorProfileImageUrl: String = "",
    val localImagePath: String = "",
    val remoteImageUrl: String = "",
    val likedAt: Long = 0L,
    val width: Int = 0,
    val height: Int = 0
)