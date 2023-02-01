package com.lelestacia.lelenimexml.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeCharacterCrossRefEntity

@Dao
interface AnimeCharacterCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: List<AnimeCharacterCrossRefEntity>)

    @Query("SELECT * FROM anime_characters_cross_ref_table WHERE anime_id =:animeID")
    fun getCharactersByAnimeID(animeID: Int): List<AnimeCharacterCrossRefEntity>
}