package com.treemiddle.photoexplorer.remote

import com.treemiddle.photoexplorer.core.common.RateLimitException
import com.treemiddle.photoexplorer.data.datasource.PhotoRemoteDataSource
import com.treemiddle.photoexplorer.data.model.PhotoData
import com.treemiddle.photoexplorer.data.model.PhotoDetailData
import com.treemiddle.photoexplorer.remote.mapper.toData
import com.treemiddle.photoexplorer.remote.model.PhotoResponse
import retrofit2.HttpException
import javax.inject.Inject

class PhotoExplorerRemoteDataSourceImpl @Inject constructor(
    private val apiService: PhotoExplorerApiService,
    private val imageDownloader: ImageDownloader
) : PhotoRemoteDataSource {
    override suspend fun getPhotoList(page: Int): PhotoData {
        val response = apiService.getPhotoList(page = page)
        if (response.isSuccessful.not()) {
            throw HttpException(response).toException()
        }

        return PhotoResponse(
            list = response.body().orEmpty(),
            hasNext = LinkHeader.hasNext(header = response.headers()["Link"])
        ).toData()
    }

    override suspend fun trackDownloadApi(id: String) {
        apiService.trackDownloadApi(id = id)
    }

    override suspend fun downloadImage(url: String): ByteArray {
        return imageDownloader.fetch(url = url)
    }

    override suspend fun getPhotoDetail(id: String): PhotoDetailData {
        return try {
            apiService.getPhotoDetail(id = id).toData()
        } catch (e: HttpException) {
            throw e.toException()
        }
    }

    private fun HttpException.toException(): Exception {
        return if (code() == RATE_LIMIT_CODE) {
            RateLimitException(cause = this)
        } else {
            this
        }
    }

    companion object {
        private const val RATE_LIMIT_CODE = 403
    }
}