package com.lelestacia.lelenimexml.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)

    @Query("SELECT * FROM character WHERE anime_id =:animeId ORDER BY name asc")
    fun getAllCharacterFromAnimeById(animeId: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAdditionalInformation(characterInformationEntity: CharacterInformationEntity)

    @Query("SELECT * FROM character_information WHERE character_id = :characterId")
    fun getCharacterAdditionalInformationById(characterId: Int): CharacterInformationEntity?

    @Transaction
    @Query("SELECT * FROM character WHERE id = :characterId")
    fun getCharacterFullProfile(characterId: Int): CharacterProfile
}
