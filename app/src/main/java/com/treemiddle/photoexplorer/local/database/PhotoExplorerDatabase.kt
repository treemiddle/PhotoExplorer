package com.treemiddle.photoexplorer.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.treemiddle.photoexplorer.local.dao.LikedPhotoDao
import com.treemiddle.photoexplorer.local.model.LikedPhotoCardEntity

@Database(
    entities = [LikedPhotoCardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PhotoExplorerDatabase : RoomDatabase() {
    abstract fun likedPhotoDao(): LikedPhotoDao

    companion object {
        const val DATABASE_NAME = "photo_explorer_database"
    }
}