package com.treemiddle.photoexplorer.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import java.io.IOException
import javax.inject.Inject

interface ImageDownloader {
    suspend fun fetch(url: String): ByteArray
}

class ImageDownloaderImpl @Inject constructor(
    private val client: OkHttpClient
) : ImageDownloader {
    override suspend fun fetch(url: String): ByteArray {
        val request = Request.Builder()
            .url(url = url)
            .build()

        return client.newCall(request = request)
            .executeAsync()
            .use { response ->
                withContext(context = Dispatchers.IO) {
                    if (response.isSuccessful.not()) {
                        throw IOException("HTTP ${response.code}")
                    }
                    response.body.bytes()
                }
            }
    }
}