package com.treemiddle.photoexplorer.remote

import com.treemiddle.photoexplorer.remote.model.PhotoDetailResponse
import com.treemiddle.photoexplorer.remote.model.PhotoInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoExplorerApiService {
    @GET(GET_PHOTOS)
    suspend fun getPhotoList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE,
    ): Response<List<PhotoInfo>>

    @GET(PHOTO_DOWNLOAD)
    suspend fun trackDownloadApi(@Path("id") id: String)

    @GET(GET_PHOTO_DETAIL)
    suspend fun getPhotoDetail(@Path("id") id: String): PhotoDetailResponse

    companion object {
        private const val GET_PHOTOS = "photos"
        private const val PHOTO_DOWNLOAD = "photos/{id}/download"
        private const val GET_PHOTO_DETAIL = "photos/{id}"

        private const val DEFAULT_PER_PAGE = 20
    }
}