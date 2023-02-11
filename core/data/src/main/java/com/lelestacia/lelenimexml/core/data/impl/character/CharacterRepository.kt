package com.lelestacia.lelenimexml.core.data.impl.character

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.*
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeCharacterReferenceEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterProfile
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterVoiceActorCrossRefEntity
import com.lelestacia.lelenimexml.core.database.entity.voice_actor.VoiceActorEntity
import com.lelestacia.lelenimexml.core.database.service.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: ICharacterNetworkService,
    private val characterDatabaseService: ICharacterDatabaseService,
    private val errorParser: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterRepository {

    override fun getAnimeCharactersByAnimeID(animeID: Int): Flow<Resource<List<Character>>> =
        flow<Resource<List<Character>>> {
            val animeCharactersCrossRef: List<AnimeCharacterReferenceEntity> =
                characterDatabaseService.getCharactersByAnimeID(animeID = animeID)

            if (animeCharactersCrossRef.isEmpty()) {

                val apiResponse: List<CharacterResponse> =
                    apiService.getCharactersByAnimeID(animeID = animeID)

                if (apiResponse.isEmpty()) {

                    /*
                     *  Returning here, because since the API doesn't return anything,
                     *  that means the data on the server are also empty
                     */

                    emit(Resource.Success(data = emptyList()))
                    return@flow
                }

                val characterEntities: List<CharacterEntity> =
                    apiResponse.map { it.asNewEntity() }

                val characterWithVoiceActors: List<Pair<Int, List<VoiceActorEntity>>> =
                    apiResponse.map { it.asCharacterWithVoiceActorEntities() }

                characterDatabaseService.insertCharactersAndVoiceActors(
                    characters = characterEntities,
                    voiceActors = characterWithVoiceActors.map { pair ->
                        pair.second
                    }.flatten(),
                    animeCharactersCrossRef = apiResponse.map { networkCharacter ->
                        AnimeCharacterReferenceEntity(
                            animeID = animeID,
                            characterID = networkCharacter.characterData.malID
                        )
                    },
                    characterVoiceActorsCrossRef = characterWithVoiceActors.map { pair ->
                        pair.second.map { voiceActorEntity ->
                            CharacterVoiceActorCrossRefEntity(
                                characterID = pair.first,
                                voiceActorID = voiceActorEntity.voiceActorID
                            )
                        }
                    }.flatten()
                ).also {
                    emit(Resource.Success(data = characterEntities.map { it.asCharacter() }))
                    return@flow
                }

                /*
                 *  At this point, characters' data, voice actors data,
                 *  and its corresponding reference have been inserted into its own table to be queried in further usage.
                 *  Also, there's no need to continue the process since the data are coming straight from the API
                 *  and don't need further inspection
                 */
            }

            val charactersID: List<Int> = animeCharactersCrossRef.map { it.characterID }
            val localCharacters: List<CharacterEntity> =
                characterDatabaseService.getCharactersByCharacterID(characterID = charactersID)
            val localVoiceActors =
                characterDatabaseService.getVoiceActorsByCharacterID(characterID = charactersID)

            emit(Resource.Success(data = localCharacters.map { it.asCharacter() }))

            val oldestUpdate: Long = localCharacters.minOf {
                (it.updatedAt ?: it.createdAt).time
            }
            val timeDifference: Long = Date().time - oldestUpdate
            val isDataOutDated = (TimeUnit.MILLISECONDS.toHours(timeDifference) % 24).toInt() > 24

            if (isDataOutDated) {
                val apiResponse = apiService.getCharactersByAnimeID(animeID = animeID)

                val newCharacters: List<CharacterEntity> =
                    apiResponse.map { it.asNewEntity() }
                val newVoiceActors: List<VoiceActorEntity> =
                    apiResponse.map { it.asNewVoiceActor() }.flatten()

                characterDatabaseService.updateCharactersAndVoiceActors(
                    characters = Pair(localCharacters, newCharacters),
                    voiceActors = Pair(localVoiceActors, newVoiceActors)
                )

                emit(Resource.Success(data = newCharacters.map { it.asCharacter() }))
            }
        }.onStart {
            emit(Resource.Loading)
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(data = null, message = errorParser(t)))
                else -> emit(Resource.Error(data = null, message = "Error: ${t.message}"))
            }
        }.flowOn(ioDispatcher)

    /*
     *  The function is designed to use both local data and network data.
     *  It will do the following process:
     *      1. The function will fetch character from local data
     *         and check whether it is empty or not
     *      2. The function will calculate the difference between the last network request based on timestamp
     *      3. The function will determine
     *         whether it should pull other data / new data from network or not
     *      4. If it's making a network call,
     *         it will insert the data with the correct timestamp tobe used again
     *         for the latter use
     */

    override fun getCharacterDetailById(characterID: Int): Flow<Resource<CharacterDetail>> =
        flow {
            var localCharacter: CharacterInformationEntity? =
                characterDatabaseService.getCharacterAdditionalInformationById(characterID)

            if (localCharacter != null) {
                val localCharacterDetail =
                    characterDatabaseService.getCharacterFullProfile(characterID = characterID)
                emit(Resource.Success(data = localCharacterDetail.asCharacterDetail()))
            }

            val oldestUpdate: Long =
                if (localCharacter == null) 0
                else {
                    (localCharacter.updatedAt ?: localCharacter.createdAt).time
                }

            val timeDifference = Date().time - oldestUpdate
            val isDataOutdated = {
                val differenceInHours = TimeUnit.MILLISECONDS.toHours(timeDifference).toInt()
                Timber.d("Difference in hours is $differenceInHours")
                differenceInHours >= 24
                true
            }

            val shouldFetchNetwork = localCharacter == null || isDataOutdated.invoke()
            if (shouldFetchNetwork) {
                emit(Resource.Loading)
                val apiResponse = apiService.getCharacterDetailByCharacterID(characterID)
                val newEntities = apiResponse.asNewEntity()

                localCharacter =
                    localCharacter?.copy(
                        characterNickNames = newEntities.characterNickNames,
                        characterFavorite = newEntities.characterFavorite,
                        characterInformation = newEntities.characterInformation,
                        updatedAt = Date()
                    ) ?: newEntities

                characterDatabaseService.insertOrUpdateAdditionalInformation(
                    characterInformationEntity = localCharacter
                )
            }

            val fullInformation =
                characterDatabaseService.getCharacterFullProfile(characterID = characterID)
            emit(Resource.Success(data = fullInformation.asCharacterDetail()))
        }.onStart {
            emit(Resource.Loading)
        }.catch { t ->

            val localCharacter: CharacterInformationEntity? =
                characterDatabaseService.getCharacterAdditionalInformationById(characterID)

            val localCharacterProfile: CharacterProfile? =
                if (localCharacter == null) null
                else characterDatabaseService.getCharacterFullProfile(characterID = characterID)

            /*
             * If a local character is null,
             * this means exception happened during a fresh pull network request,
             * but if it is existed,
             * then it means the exception was happened during update network request
             */

            when (t) {
                is HttpException -> emit(
                    Resource.Error(
                        data = localCharacterProfile?.asCharacterDetail(),
                        message = errorParser(t)
                    )
                )
                else -> emit(
                    Resource.Error(
                        data = localCharacterProfile?.asCharacterDetail(),
                        message = "Error: ${t.message}"
                    )
                )
            }
        }.flowOn(ioDispatcher)
}
