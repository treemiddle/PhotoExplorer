package com.treemiddle.photoexplorer.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class PhotoExplorerInterceptor(val accessKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url.host != UNSPLASH_API_HOST) {
            return chain.proceed(request = original)
        }

        val authorized = original
            .newBuilder()
            .header(
                name = AUTHORIZATION,
                value = "$CLIENT_ID $accessKey"
            ).header(
                name = ACCEPT_VERSION,
                value = VERSION
            ).build()

        return chain.proceed(request = authorized)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val ACCEPT_VERSION = "Accept-Version"
        private const val VERSION = "v1"
        private const val CLIENT_ID = "Client-ID"

        private const val UNSPLASH_API_HOST = "api.unsplash.com"
    }
}