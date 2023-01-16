package com.lelestacia.lelenimexml.core.database

import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity

interface ICharacterLocalDataSource {
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)
    suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterAdditionalInformationEntity)
    suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterAdditionalInformationEntity?
    suspend fun getCharacterFullProfile(characterID: Int): CharacterDetailEntity
    suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity>
}