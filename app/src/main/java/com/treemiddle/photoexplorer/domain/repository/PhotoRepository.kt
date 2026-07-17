package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.PhotoData

interface PhotoRepository {
    suspend fun getPhotoList(page: Int): PhotoData
    suspend fun trackDownloadApi(id: String)
}