package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.data.utility.asCharacter
import com.lelestacia.lelenimexml.core.data.utility.asCharacterDetail
import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.ICharacterLocalDataSource
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: INetworkDataSource,
    private val localDataSource: ICharacterLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterRepository {

    override fun getAnimeCharactersById(animeID: Int): Flow<List<Character>> = flow {
        var localCharacter: List<CharacterEntity> =
            localDataSource.getAllCharacterFromAnimeById(animeID)

        val shouldFetchNetwork = localCharacter.isEmpty()
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService.getCharactersByAnimeID(animeID)
                .map { characterResponse ->
                    characterResponse.asEntity(animeID)
                }
            localDataSource.insertOrUpdateCharacter(apiResponse)
            localCharacter = localDataSource.getAllCharacterFromAnimeById(animeID)
        }

        emit(
            localCharacter.map {
                it.asCharacter()
            }
        )
    }.flowOn(ioDispatcher)

    override fun getCharacterDetailById(characterID: Int): Flow<CharacterDetail> = flow {
        val localCharacterAdditionalInfo: CharacterInformationEntity? =
            localDataSource.getCharacterAdditionalInformationById(characterID)

        val shouldFetchNetwork = localCharacterAdditionalInfo == null
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService
                .getCharacterDetailByCharacterID(characterID)

            val additionalInformationEntity = apiResponse.asEntity()
            localDataSource.insertOrReplaceAdditionalInformation(additionalInformationEntity)
        }

        emit(localDataSource.getCharacterFullProfile(characterID).asCharacterDetail())
    }.flowOn(ioDispatcher)
}
