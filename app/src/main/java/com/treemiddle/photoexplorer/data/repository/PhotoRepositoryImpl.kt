package com.treemiddle.photoexplorer.data.repository

import com.treemiddle.photoexplorer.data.datasource.PhotoRemoteDataSource
import com.treemiddle.photoexplorer.data.mapper.toDomain
import com.treemiddle.photoexplorer.domain.model.PhotoData
import com.treemiddle.photoexplorer.domain.model.PhotoDetail
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: PhotoRemoteDataSource
) : PhotoRepository {
    override suspend fun getPhotoList(page: Int): PhotoData {
        return remoteDataSource.getPhotoList(page = page).toDomain()
    }

    override suspend fun trackDownloadApi(id: String) {
        remoteDataSource.trackDownloadApi(id = id)
    }

    override suspend fun getPhotoDetail(id: String): PhotoDetail {
        return remoteDataSource.getPhotoDetail(id = id).toDomain()
    }
}