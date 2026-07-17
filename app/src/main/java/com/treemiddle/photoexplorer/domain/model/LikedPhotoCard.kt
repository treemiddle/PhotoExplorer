package com.treemiddle.photoexplorer.domain.model

data class LikedPhotoCard(
    val id: String = "",
    val description: String = "",
    val authorName: String = "",
    val authorProfileImageUrl: String = "",
    val localImagePath: String = "",
    val remoteImageUrl: String = "",
    val likedAt: Long = 0L,
    val isLiked: Boolean = true
)

data class LikedPhotoRequest(
    val id: String,
    val description: String,
    val authorName: String,
    val authorProfileImageUrl: String,
    val imageUrl: String
)

fun PhotoInfo.toLikedPhotoRequest(): LikedPhotoRequest {
    return LikedPhotoRequest(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        imageUrl = regularUrl
    )
}

fun LikedPhotoCard.toLikedPhotoRequest(): LikedPhotoRequest {
    return LikedPhotoRequest(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        imageUrl = localImagePath
    )
}