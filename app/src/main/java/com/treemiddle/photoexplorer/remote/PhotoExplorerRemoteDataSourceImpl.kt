package com.treemiddle.photoexplorer.remote

import com.treemiddle.photoexplorer.data.datasource.PhotoExplorerRemoteDataSource
import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.remote.mapper.toData
import com.treemiddle.photoexplorer.remote.model.PhotoResponse
import retrofit2.HttpException
import javax.inject.Inject

class PhotoExplorerRemoteDataSourceImpl @Inject constructor(
    private val apiService: PhotoExplorerApiService
) : PhotoExplorerRemoteDataSource {
    override suspend fun getPhotoList(page: Int): PhotoData {
        val response = apiService.getPhotoList(page = page)
        if (response.isSuccessful.not()) {
            throw HttpException(response)
        }

        return PhotoResponse(
            list = response.body().orEmpty(),
            hasNext = LinkHeader.hasNext(header = response.headers()["Link"])
        ).toData()
    }
}