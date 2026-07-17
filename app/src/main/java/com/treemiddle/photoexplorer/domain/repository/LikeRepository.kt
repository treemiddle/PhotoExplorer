package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.PhotoInfo

interface LikeRepository {
    suspend fun addPhoto(photoInfo: PhotoInfo)
}