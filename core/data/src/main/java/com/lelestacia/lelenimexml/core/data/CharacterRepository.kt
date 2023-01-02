package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.data.utility.asCharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.data.utility.asCharacterEntity
import com.lelestacia.lelenimexml.core.database.ILocalDataSource
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
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
    private val localDataSource: ILocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterRepository {

    override fun getAnimeCharactersById(animeID: Int): Flow<List<CharacterEntity>> = flow {
        var localCharacter: List<CharacterEntity> =
            localDataSource.getAllCharacterFromAnimeById(animeID)

        val shouldFetchNetwork = localCharacter.isEmpty()
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService.getCharactersByAnimeID(animeID)
                .map { characterResponse ->
                    characterResponse.asCharacterEntity(animeID)
                }
            localDataSource.insertOrUpdateCharacter(apiResponse)
            localCharacter = localDataSource.getAllCharacterFromAnimeById(animeID)
        }

        emit(localCharacter)
    }.flowOn(ioDispatcher)

    override fun getCharacterDetailById(characterID: Int): Flow<CharacterDetailEntity> = flow {
        val localCharacterAdditionalInfo: CharacterAdditionalInformationEntity? =
            localDataSource.getCharacterAdditionalInformationById(characterID)

        val shouldFetchNetwork = localCharacterAdditionalInfo == null
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService
                .getCharacterDetailByCharacterID(characterID)

            val additionalInformationEntity = apiResponse.asCharacterAdditionalInformationEntity()
            localDataSource.insertOrReplaceAdditionalInformation(additionalInformationEntity)
        }

        emit(localDataSource.getCharacterFullProfile(characterID))
    }.flowOn(ioDispatcher)
}
