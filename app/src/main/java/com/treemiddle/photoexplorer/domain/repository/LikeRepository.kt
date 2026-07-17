package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.LikedPhotoRequest
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    val likedIds: Flow<Set<String>>

    suspend fun updatePhoto(photo: LikedPhotoRequest)
    suspend fun getLikedPhotoList(
        limit: Int,
        offset: Int
    ): List<LikedPhotoCard>
}