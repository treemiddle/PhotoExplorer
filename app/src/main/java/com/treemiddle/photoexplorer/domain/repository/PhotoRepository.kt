package com.treemiddle.photoexplorer.domain.repository

import com.treemiddle.photoexplorer.domain.model.PhotoData
import com.treemiddle.photoexplorer.domain.model.PhotoDetail

interface PhotoRepository {
    suspend fun getPhotoList(page: Int): PhotoData
    suspend fun trackDownloadApi(id: String)
    suspend fun getPhotoDetail(id: String): PhotoDetail
}