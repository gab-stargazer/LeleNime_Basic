package com.lelestacia.lelenimexml.core.data.impl.character

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.asCharacter
import com.lelestacia.lelenimexml.core.data.utility.asCharacterDetail
import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.INetworkCharacterService
import com.lelestacia.lelenimexml.core.network.model.ErrorResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: INetworkCharacterService,
    private val localDataSource: ICharacterDatabaseService,
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
                delay(200)
                localDataSource.insertOrUpdateCharacter(apiResponse)
                localCharacter = localDataSource.getAllCharacterFromAnimeById(animeID)
            }

            emit(
                Resource.Success(
                    localCharacter.map {
                        it.asCharacter()
                    }
                )
            )
        }.onStart { emit(Resource.Loading) }
            .catch { t ->
                when (t) {
                    is HttpException -> {
                        emit(Resource.Error(null, parseHttpError(t)))
                    }
                    else -> emit(Resource.Error(null, t.message))
                }
            }.flowOn(ioDispatcher)

    override fun getCharacterDetailById(characterID: Int): Flow<Resource<CharacterDetail>> =
        flow<Resource<CharacterDetail>> {
            Timber.d("Character ID: $characterID")
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
                is HttpException -> {
                    emit(Resource.Error(null, parseHttpError(t)))
                }
                else -> emit(Resource.Error(null, t.message))
            }
        }.flowOn(ioDispatcher)

    private fun parseHttpError(t: HttpException): String {
        val body = t.response()?.errorBody()
        val gson = Gson()
        val adapter: TypeAdapter<ErrorResponse> = gson.getAdapter(ErrorResponse::class.java)
        return try {
            val error = adapter.fromJson(body?.string())
            "Error ${error.status}: ${error.message}"
        } catch (e: Exception) {
            "Response failed to parse"
        }
    }
}
