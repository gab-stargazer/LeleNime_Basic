package com.lelestacia.lelenimexml.core.data.impl.anime

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.common.Constant.IS_NSFW
import com.lelestacia.lelenimexml.core.data.utility.asAnime
import com.lelestacia.lelenimexml.core.data.utility.asNewEntity
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.service.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeApiService: IAnimeNetworkService,
    private val animeDatabaseService: IAnimeDatabaseService,
    private val userPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeRepository {

    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.getAiringAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        val isNsfw = isNsfwMode()
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.searchAnimeByTitle(query, isNsfw)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime> =
        animeDatabaseService.getNewestAnimeDataByAnimeID(animeID = animeID).map { it.asAnime() }

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                animeDatabaseService.getAnimeHistory()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun insertAnimeToHistory(anime: Anime) {
        withContext(ioDispatcher) {
            val localAnime: AnimeEntity? = animeDatabaseService.getAnimeByAnimeID(anime.malID)
            val isAnimeExist = localAnime != null

            if (isAnimeExist) {
                val updatedHistory: AnimeEntity = (localAnime as AnimeEntity).copy(
                    lastViewed = Date(),
                )
                animeDatabaseService.updateAnime(updatedHistory)
                return@withContext
            }

            val newAnime = anime.asNewEntity()
            animeDatabaseService.insertOrReplaceAnime(newAnime)
        }
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                animeDatabaseService.getAnimeFavorite()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun updateAnimeFavorite(malID: Int) {
        withContext(ioDispatcher) {
            val anime = animeDatabaseService.getAnimeByAnimeID(animeID = malID)
            anime?.let { oldAnime ->
                val newAnime = oldAnime.copy(
                    isFavorite = !oldAnime.isFavorite,
                    updatedAt = Date()
                )
                animeDatabaseService.updateAnime(newAnime)
            }
        }
    }

    override fun isNsfwMode(): Boolean {
        return userPreferences.getBoolean(IS_NSFW, false)
    }

    override fun changeSafeMode(isNsfw: Boolean) {
        userPreferences
            .edit()
            .putBoolean(IS_NSFW, isNsfw)
            .apply()
    }
}
