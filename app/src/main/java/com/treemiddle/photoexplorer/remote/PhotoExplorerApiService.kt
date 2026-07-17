package com.treemiddle.photoexplorer.remote

import com.treemiddle.photoexplorer.remote.model.PhotoInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoExplorerApiService {
    @GET(GET_PHOTOS)
    suspend fun getPhotoList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE,
    ): Response<List<PhotoInfo>>

    companion object {
        private const val GET_PHOTOS = "photos"

        private const val DEFAULT_PER_PAGE = 20
    }
}