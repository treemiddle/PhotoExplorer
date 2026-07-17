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
        it.toData()
    }
}

fun LikedPhotoCardEntity.toData(): LikedPhotoData {
    return LikedPhotoData(
        id = id,
        description = description,
        authorName = authorName,
        authorProfileImageUrl = authorProfileImageUrl,
        localImagePath = localImagePath,
        remoteImageUrl = remoteImageUrl,
        likedAt = likedAt
    )
}