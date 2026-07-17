package com.treemiddle.photoexplorer.data.datasource

import com.treemiddle.photoexplorer.data.model.LikedPhotoData

interface PhotoLocalDataSource {
    suspend fun addPhotoCard(photoCard: LikedPhotoData)
    suspend fun hasId(id: String): Boolean
    suspend fun saveImage(
        photoId: String,
        byteArray: ByteArray
    ): String
    suspend fun deleteImage(id: String)
}