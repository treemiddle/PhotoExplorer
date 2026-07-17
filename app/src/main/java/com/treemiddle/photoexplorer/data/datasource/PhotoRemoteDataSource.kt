package com.treemiddle.photoexplorer.data.datasource

import com.treemiddle.photoexplorer.data.model.PhotoData

interface PhotoExplorerRemoteDataSource {
    suspend fun getPhotoList(page: Int): PhotoData
    suspend fun downloadPhoto(id: String)
}