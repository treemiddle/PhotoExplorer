package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    val likedIds: Flow<Set<String>>

    suspend fun addPhoto(photoInfo: PhotoInfo)
}