package com.lelestacia.lelenimexml.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.database.ILocalDataSource
import com.lelestacia.lelenimexml.core.database.user_pref.UserPref
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import com.lelestacia.lelenimexml.core.model.domain.anime.asEntity
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: INetworkDataSource,
    private val localDataSource: ILocalDataSource,
    private val userPref: UserPref
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
        val isSafeMode = userPref.isSafeMode()
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

    override fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity> {
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

    override suspend fun insertAnimeToHistory(anime: Anime) {
        val localAnime = localDataSource.getAnimeByAnimeId(anime.malID)
        val isExist = localAnime != null

        if (isExist) {
            val newAnime = anime.asEntity(
                isFavorite = (localAnime as AnimeEntity).isFavorite
            )
            localDataSource.insertOrUpdateAnime(newAnime)
            Timber.d("Anime Updated")
            return
        }

        val newAnime = anime.asEntity()
        localDataSource.insertOrUpdateAnime(newAnime)
        Timber.d("Anime Inserted")
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
            localDataSource.updateAnime(
                anime.apply {
                    isFavorite = !isFavorite
                }
            )
        }
    }

    override fun isSafeMode(): Boolean = userPref.isSafeMode()

    override fun changeSafeMode(isSafeMode: Boolean) {
        userPref.switchSafeMode(isSafeMode)
    }
}
