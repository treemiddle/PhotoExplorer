package com.treemiddle.photoexplorer.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_photo_card")
data class LikedPhotoCardEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val authorName: String,
    val authorProfileImageUrl: String,
    val localImagePath: String,
    val remoteImageUrl: String,
    val likedAt: Long,
    val width: Int,
    val height: Int
)