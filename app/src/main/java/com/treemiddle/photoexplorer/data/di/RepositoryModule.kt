package com.treemiddle.photoexplorer.data.di

import com.treemiddle.photoexplorer.data.repository.LikeRepositoryImpl
import com.treemiddle.photoexplorer.data.repository.PhotoRepositoryImpl
import com.treemiddle.photoexplorer.domain.repository.LikeRepository
import com.treemiddle.photoexplorer.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @Singleton
    fun bindsLikeRepository(impl: LikeRepositoryImpl): LikeRepository
}