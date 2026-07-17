package com.treemiddle.photoexplorer.local.di

import android.content.Context
import androidx.room.Room
import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.local.PhotoLocalDataSourceImpl
import com.treemiddle.photoexplorer.local.dao.LikedPhotoDao
import com.treemiddle.photoexplorer.local.database.PhotoExplorerDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalModule {
    @Binds
    @Singleton
    fun bindsPhotoLocalDataSource(impl: PhotoLocalDataSourceImpl): PhotoLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun providesPhotoExplorerDatabase(@ApplicationContext context: Context): PhotoExplorerDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = PhotoExplorerDatabase::class.java,
                name = PhotoExplorerDatabase.DATABASE_NAME
            ).build()
        }

        @Provides
        @Singleton
        fun providesLikedPhotoDao(database: PhotoExplorerDatabase): LikedPhotoDao {
            return database.likedPhotoDao()
        }
    }
}