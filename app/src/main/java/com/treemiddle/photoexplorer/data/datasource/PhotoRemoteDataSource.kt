package com.treemiddle.photoexplorer.data.datasource

import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.data.model.PhotoDetailData

interface PhotoRemoteDataSource {
    suspend fun getPhotoList(page: Int): PhotoData
    suspend fun trackDownloadApi(id: String)
    suspend fun downloadImage(url: String): ByteArray
    suspend fun getPhotoDetail(id: String): PhotoDetailData
}