package com.treemiddle.photoexplorer.data.datasource

import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import kotlinx.coroutines.flow.Flow

interface PhotoLocalDataSource {
    val likedIds: Flow<Set<String>>

    fun observeIsLiked(id: String): Flow<Boolean>
    fun observeLikedPhotoList(limit: Int): Flow<List<LikedPhotoData>>

    suspend fun addPhotoCard(photoCard: LikedPhotoData)
    suspend fun hasId(id: String): Boolean
    suspend fun saveImage(
        photoId: String,
        byteArray: ByteArray
    ): String
    suspend fun deleteImage(id: String)
    suspend fun getLikedPhoto(id: String): LikedPhotoData?
}