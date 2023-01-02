package com.lelestacia.lelenimexml.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)

    @Query("SELECT * FROM character_table WHERE animeId =:animeId ORDER BY characterName asc")
    fun getAllCharacterFromAnimeById(animeId: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAdditionalInformation(characterAdditionalInformationEntity: CharacterAdditionalInformationEntity)

    @Query("SELECT * FROM character_additional_information_table WHERE characterId = :characterId")
    fun getCharacterAdditionalInformationById(characterId: Int): CharacterAdditionalInformationEntity?

    @Transaction
    @Query("SELECT * FROM character_table WHERE characterId = :characterId")
    fun getCharacterFullProfile(characterId: Int): CharacterDetailEntity
}
