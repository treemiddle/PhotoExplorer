package com.treemiddle.photoexplorer.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treemiddle.photoexplorer.local.model.LikedPhotoCardEntity

@Dao
interface LikedPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(photo: LikedPhotoCardEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM liked_photo_card WHERE id = :id)")
    suspend fun hasId(id: String): Boolean

    @Query("DELETE FROM liked_photo_card WHERE id = :id")
    suspend fun delete(id: String)
}