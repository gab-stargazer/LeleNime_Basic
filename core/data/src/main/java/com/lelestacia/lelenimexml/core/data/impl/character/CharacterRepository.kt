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
import timber.log.Timber
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

            val shouldFetchNetwork = localCharacter.isEmpty()
            if (shouldFetchNetwork) {
                val apiResponse =
                    apiService.getCharactersByAnimeID(animeID).map { characterResponse ->
                        characterResponse.asEntity(animeID)
                    }
                delay(500)
                localDataSource.insertOrUpdateCharacter(apiResponse)
                localCharacter = localDataSource.getAllCharacterFromAnimeById(animeID)
            }

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

    override fun getCharacterDetailById(characterID: Int): Flow<Resource<CharacterDetail>> =
        flow<Resource<CharacterDetail>> {
            val localCharacterAdditionalInfo: CharacterInformationEntity? =
                localDataSource.getCharacterAdditionalInformationById(characterID)

            val shouldFetchNetwork = localCharacterAdditionalInfo == null
            if (shouldFetchNetwork) {
                val apiResponse = apiService.getCharacterDetailByCharacterID(characterID)
                Timber.d("Response: $apiResponse")
                val additionalInformationEntity = apiResponse.asEntity()
                localDataSource.insertOrReplaceAdditionalInformation(additionalInformationEntity)
                delay(500)
            }

            val characterDetail =
                localDataSource.getCharacterFullProfile(characterID).asCharacterDetail()
            emit(Resource.Success(characterDetail))
        }.onStart {
            emit(Resource.Loading)
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(data = null, message = errorParser(t)))
                else -> emit(Resource.Error(data = null, message = "Error: ${t.message}"))
            }
        }.flowOn(ioDispatcher)
}
