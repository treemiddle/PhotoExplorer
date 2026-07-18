package com.treemiddle.photoexplorer.data.repository

import com.treemiddle.photoexplorer.data.datasource.PhotoExplorerRemoteDataSource
import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.data.mapper.toData
import com.treemiddle.photoexplorer.data.mapper.toDomain
import com.treemiddle.photoexplorer.domain.model.LikedPhotoCard
import com.treemiddle.photoexplorer.domain.model.LikedPhotoRequest
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val remoteDataSource: PhotoExplorerRemoteDataSource,
    private val localDataSource: PhotoLocalDataSource
) : LikeRepository {
    override val likedIds: Flow<Set<String>> = localDataSource.likedIds

    private val likeIdHashMap = ConcurrentHashMap<String, Mutex>()

    override fun observeIsLiked(id: String): Flow<Boolean> = localDataSource.observeIsLiked(id = id)

    override suspend fun like(photo: LikedPhotoRequest) {
        mutexOf(id = photo.id).withLock {
            if (localDataSource.hasId(id = photo.id)) {
                return@withLock
            }
            savePhoto(photo = photo)
        }
    }

    override suspend fun unlike(photoId: String) {
        mutexOf(id = photoId).withLock {
            if (localDataSource.hasId(id = photoId)) {
                localDataSource.deleteImage(id = photoId)
            }
        }
    }

    override suspend fun getLikedPhotoList(
        limit: Int,
        offset: Int
    ): List<LikedPhotoCard> {
        return localDataSource.getLikedPhotoList(
            limit = limit,
            offset = offset
        ).toDomain()
    }

    override suspend fun getLikedPhoto(photoId: String): LikedPhotoCard? {
        return localDataSource.getLikedPhoto(id = photoId)?.toDomain()
    }

    private suspend fun savePhoto(photo: LikedPhotoRequest) {
        runCatching {
            remoteDataSource.trackDownloadApi(id = photo.id)
        }
        try {
            val byteArray = remoteDataSource.downloadImage(url = photo.imageUrl)
            val path = localDataSource.saveImage(
                photoId = photo.id,
                byteArray = byteArray
            )

            localDataSource.addPhotoCard(
                photo.toData(
                    localImagePath = path,
                    likedAt = System.currentTimeMillis()
                )
            )
        } catch (t: Throwable) {
            withContext(NonCancellable) {
                localDataSource.deleteImage(id = photo.id)
            }
            throw t
        }
    }

    private fun mutexOf(id: String): Mutex {
        return likeIdHashMap.getOrPut(
            key = id,
            defaultValue = {
                Mutex()
            }
        )
    }
}