package com.lelestacia.lelenimexml.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEpisode(episodes: List<EpisodeEntity>)

    @Query("SELECT * FROM episode WHERE anime_id = :animeID ORDER BY mal_id ASC")
    fun getEpisodeByAnimeID(animeID: Int): List<EpisodeEntity>
}
