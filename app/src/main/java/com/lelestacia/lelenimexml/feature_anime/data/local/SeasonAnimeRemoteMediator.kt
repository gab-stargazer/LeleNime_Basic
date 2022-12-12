package com.lelestacia.lelenimexml.feature_anime.data.local

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lelestacia.lelenimexml.core.local.database.AnimeDatabase
import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeKeyEntity
import com.lelestacia.lelenimexml.core.network.api.JikanAPI
import com.lelestacia.lelenimexml.feature_anime.utility.SeasonAnimeMapper
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SeasonAnimeRemoteMediator(
    private val database: AnimeDatabase,
    private val apiService: JikanAPI,
    private val mContext: Context
) : RemoteMediator<Int, SeasonAnimeEntity>() {

    private val seasonAnimeDao = database.seasonAnimeDao()
    private val seasonAnimeKeyDao = database.seasonAnimeKeyDao()

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, SeasonAnimeEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prefKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val apiResponse = apiService.getCurrentSeason(page)
            Timber.d("API Service returned ${apiResponse.data.size} amount of data")

            val endOfPaginationReached = !apiResponse.pagination.hasNextPage
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    seasonAnimeKeyDao.clearSeasonAnimeKey()
                    seasonAnimeDao.clearSeasonAnime()
                }

                val prefKey = if (page == 1) null else page - 1
                val nextKey = if (apiResponse.pagination.hasNextPage) page.plus(1) else null

                apiResponse.data.map {
                    SeasonAnimeKeyEntity(id = it.malId, prefKey = prefKey, nextKey = nextKey)
                }.also {
                    seasonAnimeKeyDao.insertOrReplaceKey(it)
                }

                apiResponse.data.map {
                    SeasonAnimeMapper.networkToEntities(it)
                }.also { listOfAnime ->
                    seasonAnimeDao.insertAll(listOfAnime)
                }

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SeasonAnimeEntity>): SeasonAnimeKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            seasonAnimeKeyDao.seasonAnimeRemoteKey(data.malId)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SeasonAnimeEntity>): SeasonAnimeKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            seasonAnimeKeyDao.seasonAnimeRemoteKey(data.malId)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SeasonAnimeEntity>): SeasonAnimeKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.malId?.let { id ->
                seasonAnimeKeyDao.seasonAnimeRemoteKey(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val LAST_NETWORK_REQUEST = "last_network_request"
        const val SHARED_PREF = "shared_pref"
    }
}