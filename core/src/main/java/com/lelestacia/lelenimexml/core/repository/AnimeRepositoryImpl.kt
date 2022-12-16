package com.lelestacia.lelenimexml.core.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterFullProfileEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import com.lelestacia.lelenimexml.core.source.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.core.source.remote.SeasonAnimePaging
import com.lelestacia.lelenimexml.core.utility.CharacterMapper
import com.lelestacia.lelenimexml.core.utility.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.utility.Constant.USER_PREF
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanAPI,
    private val animeDatabase: AnimeDatabase,
    private val mContext: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AnimeRepository {

    override fun seasonAnimePagingData(): Flow<PagingData<AnimeResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SeasonAnimePaging(apiService)
            }
        ).flow
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<AnimeResponse>> {
        val isSafeMode = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
            .getBoolean(IS_SFW, true)
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchAnimePaging(query, apiService, isSafeMode)
            }
        ).flow
    }

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDatabase.animeDao().insertOrUpdateAnime(anime)
        }
    }

    override fun getAnimeHistory(): Flow<PagingData<AnimeEntity>> =
        Pager(
            config = PagingConfig(15),
            pagingSourceFactory = {
                animeDatabase.animeDao().getAllAnime()
            }
        ).flow


    override fun getAnimeCharactersById(id: Int): Flow<List<CharacterEntity>> = flow {
        val localCharacter = animeDatabase.characterDao().getAllCharacterFromAnimeById(id)
        emit(localCharacter)

        val shouldFetchNetwork = localCharacter.isEmpty()
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService.getAnimeCharacters(id).data.map { networkCharacter ->
                CharacterMapper.networkToEntity(animeId = id, response = networkCharacter)
            }
            animeDatabase.characterDao().insertOrReplaceCharacter(apiResponse)
            emit(apiResponse)
        }
    }.flowOn(ioDispatcher)

    override fun getAnimeCharacterFullProfileByCharacterId(characterId: Int)
            : Flow<CharacterFullProfileEntity> = flow {
        val localCharacterAdditionalInfo = animeDatabase.characterDao()
            .getCharacterAdditionalInformationById(characterId)
        Timber.d(localCharacterAdditionalInfo.toString())

        val shouldFetchNetwork = localCharacterAdditionalInfo == null
        if (shouldFetchNetwork) {
            delay(200)
            val apiResponse = apiService
                .getCharacterInformationById(characterId)

            Timber.d(apiResponse.data.toString())

            val additionalInformationEntity = CharacterMapper
                .characterDetailResponseToCharacterAdditionalInformationEntity(
                    response = apiResponse.data
                )
            Timber.d(additionalInformationEntity.toString())

            animeDatabase
                .characterDao()
                .insertOrReplaceAdditionalInformation(additionalInformationEntity)

            emit(
                animeDatabase
                    .characterDao()
                    .getCharacterFullProfile(characterId)
            )
        } else {
            emit(
                animeDatabase
                    .characterDao()
                    .getCharacterFullProfile(characterId)
            )
        }
    }.flowOn(ioDispatcher)
}