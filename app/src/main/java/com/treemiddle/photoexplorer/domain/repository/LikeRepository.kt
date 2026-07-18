package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.LikedPhotoRequest
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    val likedIds: Flow<Set<String>>

    fun observeIsLiked(id: String): Flow<Boolean>
    fun observeLikedPhotoList(limit: Int): Flow<List<LikedPhotoCard>>

    suspend fun like(photo: LikedPhotoRequest)
    suspend fun unlike(photoId: String)
    suspend fun getLikedPhoto(photoId: String): LikedPhotoCard?
}