package com.lelestacia.lelenimexml.core.source.local

import androidx.room.*
import com.lelestacia.lelenimexml.core.model.local.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterFullProfileEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCharacter(character: List<CharacterEntity>)

    @Query("SELECT * FROM character_table WHERE animeId =:animeId ORDER BY characterName asc")
    fun getAllCharacterFromAnimeById(animeId: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAdditionalInformation(characterAdditionalInformationEntity: CharacterAdditionalInformationEntity)

    @Query("SELECT * FROM character_additional_information_table WHERE characterId = :characterId")
    fun getCharacterAdditionalInformationById(characterId: Int): CharacterAdditionalInformationEntity?

    @Transaction
    @Query("SELECT * FROM character_table WHERE characterId = :characterId")
    fun getCharacterFullProfile(characterId: Int): CharacterFullProfileEntity
}