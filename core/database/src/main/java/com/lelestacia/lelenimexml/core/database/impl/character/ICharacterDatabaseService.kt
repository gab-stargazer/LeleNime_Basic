package com.lelestacia.lelenimexml.core.database.impl.character

import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile

interface ICharacterDatabaseService {
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)
    suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterInformationEntity)
    suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterInformationEntity?
    suspend fun getCharacterFullProfile(characterID: Int): CharacterProfile
    suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity>
}
