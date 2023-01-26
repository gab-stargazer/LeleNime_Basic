package com.lelestacia.lelenimexml.core.database.impl.character

import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterDatabaseService @Inject constructor(
    private val characterDao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterDatabaseService {

    override suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>) {
        withContext(ioDispatcher) {
            characterDao.insertOrUpdateCharacter(character)
        }
    }

    override suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterInformationEntity) {
        withContext(ioDispatcher) {
            characterDao.insertOrReplaceAdditionalInformation(additionalInformation)
        }
    }

    override suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterInformationEntity? {
        return withContext(ioDispatcher) {
            characterDao.getCharacterAdditionalInformationById(characterID)
        }
    }

    override suspend fun getCharacterFullProfile(characterID: Int): CharacterProfile {
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
