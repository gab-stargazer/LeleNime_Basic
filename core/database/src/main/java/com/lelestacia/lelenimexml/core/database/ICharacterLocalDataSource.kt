package com.lelestacia.lelenimexml.core.database

import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity

interface ICharacterLocalDataSource {
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)
    suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterInformationEntity)
    suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterInformationEntity?
    suspend fun getCharacterFullProfile(characterID: Int): CharacterProfile
    suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity>
}