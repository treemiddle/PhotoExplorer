package com.treemiddle.photoexplorer.data.repository

import com.treemiddle.photoexplorer.data.datasource.PhotoExplorerRemoteDataSource
import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.data.mapper.toData
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.domain.model.toLikedPhotoRequest
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val remoteDataSource: PhotoExplorerRemoteDataSource,
    private val localDataSource: PhotoLocalDataSource
) : LikeRepository {
    private val likeIdHashMap = ConcurrentHashMap<String, Mutex>()

    override suspend fun addPhoto(photoInfo: PhotoInfo) {
        val mutex = likeIdHashMap.getOrPut(
            key = photoInfo.id,
            defaultValue = {
                Mutex()
            }
        )
        mutex.withLock {
            if (localDataSource.hasId(id = photoInfo.id)) {
                // NOTE : 좋아요 해제
            } else {
                like(photoInfo = photoInfo)
            }
        }
    }

    private suspend fun like(photoInfo: PhotoInfo) {
        val request = photoInfo.toLikedPhotoRequest()
        runCatching {
            val byteArray = remoteDataSource.downloadImage(url = request.imageUrl)
            val path = localDataSource.saveImage(
                photoId = request.id,
                byteArray = byteArray
            )

            localDataSource.addPhotoCard(
                request.toData(
                    localImagePath = path,
                    likedAt = System.currentTimeMillis()
                )
            )
        }.onFailure {
            localDataSource.deleteImage(id = request.id)
        }
    }
}