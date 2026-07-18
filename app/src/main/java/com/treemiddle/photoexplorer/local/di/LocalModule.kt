package com.treemiddle.photoexplorer.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.treemiddle.photoexplorer.data.datasource.LayoutLocalDataSource
import com.treemiddle.photoexplorer.data.datasource.PhotoLocalDataSource
import com.treemiddle.photoexplorer.local.LayoutLocalDataSourceImpl
import com.treemiddle.photoexplorer.local.PhotoLocalDataSourceImpl
import com.treemiddle.photoexplorer.local.PreferencesDataStoreImpl
import com.treemiddle.photoexplorer.local.dao.LikedPhotoDao
import com.treemiddle.photoexplorer.local.database.PhotoExplorerDatabase
import com.treemiddle.photoexplorer.local.datastore.PreferencesDataStore
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

    @Binds
    @Singleton
    fun bindsLayoutLocalDataSource(impl: LayoutLocalDataSourceImpl): LayoutLocalDataSource

    @Binds
    @Singleton
    fun bindsPreferencesDataStore(impl: PreferencesDataStoreImpl): PreferencesDataStore

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

        @Provides
        @Singleton
        fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = {
                    context.preferencesDataStoreFile(PreferencesDataStoreImpl.PREFERENCES_NAME)
                }
            )
        }
    }
}