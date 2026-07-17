package com.treemiddle.photoexplorer.domain.model

data class LikedPhotoCard(
    val id: String = "",
    val description: String = "",
    val authorName: String = "",
    val authorProfileImageUrl: String = "",
    val localImagePath: String = "",
    val remoteImageUrl: String = "",
    val likedAt: Long = 0L,
    val isLiked: Boolean = true,
    val width: Int = 0,
    val height: Int = 0
) {
    val ratio: Float = if (width > 0 && height > 0) {
        width.toFloat() / height.toFloat()
    } else {
        1f
    }
}

data class LikedPhotoRequest(
    val id: String,
    val description: String,
    val authorName: String,
    val authorProfileImageUrl: String,
    val imageUrl: String,
    val width: Int,
    val height: Int
)

fun PhotoInfo.toLikedPhotoRequest(): LikedPhotoRequest {
    return LikedPhotoRequest(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        imageUrl = regularUrl,
        width = width,
        height = height
    )
}

fun LikedPhotoCard.toLikedPhotoRequest(): LikedPhotoRequest {
    return LikedPhotoRequest(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        imageUrl = localImagePath,
        width = width,
        height = height
    )
}