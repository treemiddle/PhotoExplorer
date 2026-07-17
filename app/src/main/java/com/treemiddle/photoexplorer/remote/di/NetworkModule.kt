package com.treemiddle.photoexplorer.remote.di

import com.treemiddle.photoexplorer.BuildConfig
import com.treemiddle.photoexplorer.data.datasource.PhotoExplorerRemoteDataSource
import com.treemiddle.photoexplorer.remote.ImageDownloader
import com.treemiddle.photoexplorer.remote.ImageDownloaderImpl
import com.treemiddle.photoexplorer.remote.PhotoExplorerApiService
import com.treemiddle.photoexplorer.remote.PhotoExplorerRemoteDataSourceImpl
import com.treemiddle.photoexplorer.remote.interceptor.PhotoExplorerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {
    @Binds
    @Singleton
    fun bindsPhotoRemoteDataSource(impl: PhotoExplorerRemoteDataSourceImpl): PhotoExplorerRemoteDataSource

    @Binds
    @Singleton
    fun bindsImageDownloader(impl: ImageDownloaderImpl): ImageDownloader

    companion object {
        private const val APPLICATION_JSON = "application/json"
        private const val BASE_URL = "https://api.unsplash.com/"

        @Provides
        @Singleton
        fun providesPhotoExplorerApiService(
            serializationConverterFactory: Converter.Factory,
            okHttpClient: OkHttpClient,
        ): PhotoExplorerApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(serializationConverterFactory)
                .client(okHttpClient)
                .build()
                .create(PhotoExplorerApiService::class.java)
        }

        @Provides
        @Singleton
        fun providesSerializationConverterFactory(): Converter.Factory {
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            return json.asConverterFactory(contentType = APPLICATION_JSON.toMediaType())
        }

        @Provides
        @Singleton
        fun providesPhotoExplorerInterceptor(): PhotoExplorerInterceptor {
            return PhotoExplorerInterceptor(BuildConfig.UNSPLASH_ACCESS_KEY)
        }

        @Provides
        @Singleton
        fun providesOkHttpClient(clientIdInterceptor: PhotoExplorerInterceptor): OkHttpClient {
            val logging = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            return OkHttpClient.Builder()
                .addInterceptor(interceptor = clientIdInterceptor)
                .addInterceptor(interceptor = logging)
                .build()
        }
    }
}
