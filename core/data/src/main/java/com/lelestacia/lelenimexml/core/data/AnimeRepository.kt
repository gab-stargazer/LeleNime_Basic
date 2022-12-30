package com.lelestacia.lelenimexml.core.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.common.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.core.database.ILocalDataSource
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: INetworkDataSource,
    private val localDataSource: ILocalDataSource,
    private val mContext: Context
) : IAnimeRepository {

    override fun seasonAnimePagingData(): Flow<PagingData<NetworkAnime>> {
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
        ).flow
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<NetworkAnime>> {
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
                apiService.searchAnimeByTitle(query, isSafeMode)
            }
        ).flow
    }

    override suspend fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity> {
        return localDataSource.getNewestAnimeDataByAnimeId(animeID)
    }

    override suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity? {
        return localDataSource.getAnimeByAnimeId(animeID)
    }


    override fun getAnimeHistory(): Flow<PagingData<AnimeEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                localDataSource.getAllAnimeHistory()
            }
        ).flow

    override suspend fun insertAnimeToHistory(animeEntity: AnimeEntity) {
        localDataSource.insertOrUpdateAnime(animeEntity)
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<AnimeEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                localDataSource.getAllFavoriteAnime()
            }
        ).flow

    override suspend fun updateAnimeFavorite(malID: Int) {
        val anime = localDataSource.getAnimeByAnimeId(malID)
        anime?.let {
            localDataSource.updateAnime(anime.apply {
                isFavorite = !isFavorite
            })
        }
    }
}