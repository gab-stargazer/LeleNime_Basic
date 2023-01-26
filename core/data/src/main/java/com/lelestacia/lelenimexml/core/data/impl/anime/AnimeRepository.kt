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
import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.impl.anime.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: IAnimeNetworkService,
    private val localDataSource: IAnimeDatabaseService,
    mContext: Context
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

    override fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime> {
        return localDataSource.getNewestAnimeDataByAnimeID(animeID).map { it.asAnime() }
    }

    override suspend fun getAnimeByAnimeID(animeID: Int): Anime? {
        return localDataSource.getAnimeByAnimeID(animeID)?.asAnime()
    }

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                localDataSource.getAllAnimeHistory()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun insertAnimeToHistory(anime: Anime) {
        val localAnime = localDataSource.getAnimeByAnimeID(anime.animeID)
        val isExist = localAnime != null

        if (isExist) {
            val newAnime = anime.asEntity(
                isFavorite = (localAnime as AnimeEntity).isFavorite
            )
            localDataSource.insertOrUpdateAnimeIntoHistory(newAnime)
            return
        }

        val newAnime = anime.asEntity()
        localDataSource.insertOrUpdateAnimeIntoHistory(newAnime)
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                localDataSource.getAllFavoriteAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun updateAnimeFavorite(malID: Int) {
        val anime = localDataSource.getAnimeByAnimeID(malID)
        anime?.let {
            localDataSource.updateAnime(
                anime.apply {
                    isFavorite = !isFavorite
                }
            )
        }
    }

    override fun isSafeMode(): Boolean {
        return sharedPreferences.getBoolean(IS_NSFW, false)
    }

    override fun changeSafeMode(isSafeMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(IS_NSFW, isSafeMode)
            .apply()
    }
}
