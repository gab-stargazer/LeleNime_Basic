package com.lelestacia.lelenimexml.core.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.model.local.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceCharacter(character: List<CharacterEntity>)

    @Query("SELECT * FROM character_table WHERE animeId =:animeId ORDER BY characterName asc")
    fun getAllCharacterFromAnimeById(animeId: Int) : List<CharacterEntity>
}