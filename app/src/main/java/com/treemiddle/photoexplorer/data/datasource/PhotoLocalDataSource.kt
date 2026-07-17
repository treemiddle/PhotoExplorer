package com.treemiddle.photoexplorer.data.datasource

import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import kotlinx.coroutines.flow.Flow

interface PhotoLocalDataSource {
    val likedIds: Flow<Set<String>>

    suspend fun addPhotoCard(photoCard: LikedPhotoData)
    suspend fun hasId(id: String): Boolean
    suspend fun saveImage(
        photoId: String,
        byteArray: ByteArray
    ): String
    suspend fun deleteImage(id: String)
    suspend fun getLikedPhotoList(offset: Int): List<LikedPhotoData>
}