package com.treemiddle.photoexplorer.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treemiddle.photoexplorer.local.model.LikedPhotoCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(photo: LikedPhotoCardEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM liked_photo_card WHERE id = :id)")
    suspend fun hasId(id: String): Boolean

    @Query("DELETE FROM liked_photo_card WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT id FROM liked_photo_card")
    fun observeLikedIds(): Flow<List<String>>

    @Query("SELECT * FROM liked_photo_card ORDER BY likedAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<LikedPhotoCardEntity>

    @Query("SELECT * FROM liked_photo_card WHERE id = :id")
    suspend fun get(id: String): LikedPhotoCardEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM liked_photo_card WHERE id = :id)")
    fun observeIsLiked(id: String): Flow<Boolean>
}