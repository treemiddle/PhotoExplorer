package com.treemiddle.photoexplorer.local

import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.data.model.LikedPhotoData
import com.treemiddle.photoexplorer.local.dao.LikedPhotoDao
import com.treemiddle.photoexplorer.local.mapper.toData
import com.treemiddle.photoexplorer.local.mapper.toLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoLocalDataSourceImpl @Inject constructor(
    private val likedPhotoDao: LikedPhotoDao,
    private val localImageStore: LocalImageStore
) : PhotoLocalDataSource {
    override val likedIds: Flow<Set<String>> = likedPhotoDao.observeLikedIds().map {
        it.toSet()
    }

    override suspend fun addPhotoCard(photoCard: LikedPhotoData) {
        likedPhotoDao.add(photoCard.toLocal())
    }

    override suspend fun hasId(id: String): Boolean {
        return likedPhotoDao.hasId(id = id)
    }

    override suspend fun saveImage(
        photoId: String,
        byteArray: ByteArray
    ): String {
        return localImageStore.save(
            id = photoId,
            byteArray = byteArray
        )
    }

    override suspend fun deleteImage(id: String) {
        likedPhotoDao.delete(id = id)
        localImageStore.delete(id = id)
    }

    override suspend fun getLikedPhotoList(
        limit: Int,
        offset: Int
    ): List<LikedPhotoData> {
        return likedPhotoDao.getPage(
            limit = limit,
            offset = offset
        ).toData()
    }

    override suspend fun getLikedPhoto(id: String): LikedPhotoData? {
        return likedPhotoDao.get(id = id)?.toData()
    }
}