package com.lelestacia.lelenimexml.core.data.impl.character

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.data.utility.asCharacter
import com.lelestacia.lelenimexml.core.data.utility.asCharacterDetail
import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.impl.character.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: ICharacterNetworkService,
    private val localDataSource: ICharacterDatabaseService,
    private val errorParser: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterRepository {

    override fun getAnimeCharactersById(animeID: Int): Flow<Resource<List<Character>>> =
        flow<Resource<List<Character>>> {
            var localCharacter: List<CharacterEntity> =
                localDataSource.getAllCharacterFromAnimeById(animeID)

            if (localCharacter.isNotEmpty()) {
                val characters = localCharacter.map { it.asCharacter() }
                emit(Resource.Success(data = characters))
                return@flow
            }

            val apiResponse = apiService
                .getCharactersByAnimeID(animeID).map { characterResponse ->
                    characterResponse.asEntity(animeID = animeID)
                }
            delay(500)
            localDataSource.insertOrUpdateCharacter(apiResponse)
            localCharacter = localDataSource.getAllCharacterFromAnimeById(animeID)

            val characters: List<Character> = localCharacter.map { it.asCharacter() }
            emit(Resource.Success(data = characters))
        }.onStart {
            emit(Resource.Loading)
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(data = null, message = errorParser(t)))
                else -> emit(Resource.Error(data = null, message = "Error: ${t.message}"))
            }
        }.flowOn(ioDispatcher)

    /*
     *  The function is designed to use both local data and network data, it will do the following process:
     *  1. Function will fetch character from local data and check whether it is empty or not
     *  2. Function will calculate the difference between last network request based on timestamp
     *  3. Function will determine whether it should pull another data / new data from network or not
     *  4. If it making a network call, it will insert the data with the correct timestamp tobe used again
     *     for the later use
     */

    override fun getCharacterDetailById(characterID: Int): Flow<Resource<CharacterDetail>> =
        flow<Resource<CharacterDetail>> {
            var localCharacter: CharacterInformationEntity? =
                localDataSource.getCharacterAdditionalInformationById(characterID)

            val oldestUpdate: Long =
                if (localCharacter == null) 0
                else {
                    (localCharacter.updatedAt ?: localCharacter.createdAt).time
                }

            val timeDifference = Date().time - oldestUpdate
            val isDataOutdated = (TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60).toInt() > 60

            val shouldFetchNetwork = localCharacter == null || isDataOutdated
            if (shouldFetchNetwork) {
                val apiResponse = apiService.getCharacterDetailByCharacterID(characterID)
                val newEntities = apiResponse.asEntity()

                localCharacter =
                    localCharacter?.copy(
                        characterNickNames = newEntities.characterNickNames,
                        characterFavorite = newEntities.characterFavorite,
                        characterInformation = newEntities.characterInformation,
                        updatedAt = Date()
                    ) ?: newEntities

                localDataSource.insertOrReplaceAdditionalInformation(additionalInformation = localCharacter)
                delay(500)
            }

            val fullInformation = localDataSource.getCharacterFullProfile(characterID = characterID)
            emit(Resource.Success(data = fullInformation.asCharacterDetail()))
        }.onStart {
            emit(Resource.Loading)
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(data = null, message = errorParser(t)))
                else -> emit(Resource.Error(data = null, message = "Error: ${t.message}"))
            }
        }.flowOn(ioDispatcher)
}
