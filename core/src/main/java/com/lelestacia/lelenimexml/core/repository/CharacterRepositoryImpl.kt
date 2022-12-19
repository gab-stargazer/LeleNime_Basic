package com.lelestacia.lelenimexml.core.repository

import com.lelestacia.lelenimexml.core.model.local.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import com.lelestacia.lelenimexml.core.utility.CharacterMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: JikanAPI,
    animeDatabase: AnimeDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterRepository {

    private val characterDao = animeDatabase.characterDao()

    override fun getAnimeCharactersById(animeId: Int): Flow<List<CharacterEntity>> = flow {
        var localCharacter: List<CharacterEntity> =
            characterDao.getAllCharacterFromAnimeById(animeId)

        val shouldFetchNetwork = localCharacter.isEmpty()
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService.getAnimeCharacters(animeId)
                .data
                .map { characterResponse ->
                    CharacterMapper.networkToEntity(
                        animeId = animeId,
                        response = characterResponse)
                }
            characterDao.insertOrReplaceCharacter(apiResponse)
            localCharacter = characterDao.getAllCharacterFromAnimeById(animeId)
        }

        emit(localCharacter)
    }.flowOn(ioDispatcher)

    override fun getCharacterDetailById(characterId: Int): Flow<CharacterDetailEntity> = flow {
        val localCharacterAdditionalInfo: CharacterAdditionalInformationEntity? =
            characterDao.getCharacterAdditionalInformationById(characterId)

        val shouldFetchNetwork = localCharacterAdditionalInfo == null
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService
                .getCharacterInformationById(characterId)

            val additionalInformationEntity = CharacterMapper
                .characterDetailResponseToCharacterAdditionalInformationEntity(
                    response = apiResponse.data
                )

            characterDao.insertOrReplaceAdditionalInformation(additionalInformationEntity)
        }

        emit(characterDao.getCharacterFullProfile(characterId))
    }.flowOn(ioDispatcher)
}