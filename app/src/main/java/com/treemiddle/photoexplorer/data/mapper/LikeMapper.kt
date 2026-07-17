package com.treemiddle.photoexplorer.data.mapper

import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import com.treemiddle.photoexplorer.domain.model.LikedPhotoRequest

fun LikedPhotoRequest.toData(
    localImagePath: String,
    likedAt: Long
): LikedPhotoData {
    return LikedPhotoData(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        localImagePath = localImagePath,
        remoteImageUrl = imageUrl,
        likedAt = likedAt
    )
}