package com.treemiddle.photoexplorer.data.mapper

import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
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

fun List<LikedPhotoData>.toDomain(): List<LikedPhotoCard> {
    return map {
        LikedPhotoCard(
            id = it.id,
            description = it.description,
            authorName = it.authorName,
            authorProfileImageUrl = it.authorProfileImageUrl,
            localImagePath = it.localImagePath,
            remoteImageUrl = it.remoteImageUrl,
            likedAt = it.likedAt
        )
    }
}