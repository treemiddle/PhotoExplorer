package com.treemiddle.photoexplorer.local.mapper

import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import com.treemiddle.photoexplorer.local.model.LikedPhotoCardEntity

fun LikedPhotoData.toLocal(): LikedPhotoCardEntity {
    return LikedPhotoCardEntity(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        localImagePath = localImagePath,
        remoteImageUrl = remoteImageUrl,
        likedAt = likedAt
    )
}

fun List<LikedPhotoCardEntity>.toData(): List<LikedPhotoData> {
    return map {
        LikedPhotoData(
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