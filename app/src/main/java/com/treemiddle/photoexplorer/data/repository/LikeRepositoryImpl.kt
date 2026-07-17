package com.treemiddle.photoexplorer.data.repository

import com.treemiddle.photoexplorer.data.datasource.PhotoExplorerRemoteDataSource
import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.data.mapper.toData
import com.treemiddle.photoexplorer.data.mapper.toDomain
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.PhotoInfo
import com.treemiddle.photoexplorer.domain.model.toLikedPhotoRequest
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val remoteDataSource: PhotoExplorerRemoteDataSource,
    private val localDataSource: PhotoLocalDataSource
) : LikeRepository {
    override val likedIds: Flow<Set<String>> = localDataSource.likedIds

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
                unlike(id = photoInfo.id)
            } else {
                like(photoInfo = photoInfo)
            }
        }
    }

    override suspend fun getLikedPhotoList(offset: Int): List<LikedPhotoCard> {
        return localDataSource.getLikedPhotoList(offset = offset).toDomain()
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

    private suspend fun unlike(id: String) {
        localDataSource.deleteImage(id = id)
    }
}