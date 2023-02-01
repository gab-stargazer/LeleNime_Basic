package com.lelestacia.lelenimexml.core.data.impl.anime

import android.content.Context
import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.common.Constant.IS_NSFW
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.core.data.utility.asAnime
import com.lelestacia.lelenimexml.core.data.utility.asNewEntity
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: IAnimeNetworkService,
    private val animeDao: AnimeDao,
    mContext: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeRepository {

    private val sharedPreferences: SharedPreferences = mContext
        .getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                apiService.getAiringAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        val isSafeMode = sharedPreferences.getBoolean(IS_NSFW, false)
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                apiService.searchAnimeByTitle(query, isSafeMode)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime> =
        animeDao.getNewestAnimeDataByAnimeId(animeId = animeID).map { it.asAnime() }

    override suspend fun getAnimeByAnimeID(animeID: Int): Anime? {
        return animeDao.getAnimeByAnimeId(animeID)?.asAnime()
    }

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                animeDao.getAllAnimeHistory()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun insertAnimeToHistory(anime: Anime) {
        withContext(ioDispatcher) {
            val localAnime: AnimeEntity? = animeDao.getAnimeByAnimeId(anime.malID)
            val isAnimeExist = localAnime != null

            if (isAnimeExist) {
                val updatedHistory: AnimeEntity = (localAnime as AnimeEntity).copy(
                    lastViewed = Date(),
                )
                animeDao.updateAnime(updatedHistory)
                return@withContext
            }

            val newAnime = anime.asNewEntity()
            animeDao.insertOrUpdateAnime(newAnime)
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
                animeDao.getAllFavoriteAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun updateAnimeFavorite(malID: Int) {
        withContext(ioDispatcher) {
            val anime = animeDao.getAnimeByAnimeId(animeID = malID)
            anime?.let { oldAnime ->
                val newAnime = oldAnime.copy(
                    isFavorite = !oldAnime.isFavorite,
                    updatedAt = Date()
                )
                animeDao.updateAnime(newAnime)
            }
        }
    }

    override fun isSafeMode(): Boolean {
        return sharedPreferences.getBoolean(IS_NSFW, false)
    }

    override fun changeSafeMode(isSafeMode: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(IS_NSFW, isSafeMode)
            .apply()
    }
}
