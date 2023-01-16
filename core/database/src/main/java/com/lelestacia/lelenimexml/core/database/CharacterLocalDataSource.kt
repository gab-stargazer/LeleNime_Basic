package com.lelestacia.lelenimexml.core.database

import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterLocalDataSource {

    override suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>) {
        withContext(ioDispatcher) {
            characterDao.insertOrUpdateCharacter(character)
        }
    }

    override suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterAdditionalInformationEntity) {
        withContext(ioDispatcher) {
            characterDao.insertOrReplaceAdditionalInformation(additionalInformation)
        }
    }

    override suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterAdditionalInformationEntity? {
        return withContext(ioDispatcher) {
            characterDao.getCharacterAdditionalInformationById(characterID)
        }
    }

    override suspend fun getCharacterFullProfile(characterID: Int): CharacterDetailEntity {
        return withContext(ioDispatcher) {
            characterDao.getCharacterFullProfile(characterID)
        }
    }

    override suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity> {
        return withContext(ioDispatcher) {
            characterDao.getAllCharacterFromAnimeById(animeID)
        }
    }
}