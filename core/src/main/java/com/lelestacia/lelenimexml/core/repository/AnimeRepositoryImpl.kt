package com.lelestacia.lelenimexml.core.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import com.lelestacia.lelenimexml.core.source.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.core.source.remote.SeasonAnimePaging
import com.lelestacia.lelenimexml.core.utility.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.utility.Constant.USER_PREF
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanAPI,
    animeDatabase: AnimeDatabase,
    private val mContext: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AnimeRepository {

    private val animeDao = animeDatabase.animeDao()

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
            animeDao.insertOrUpdateAnime(anime)
        }
    }

    override fun getAnimeHistory(): Flow<PagingData<AnimeEntity>> =
        Pager(
            config = PagingConfig(15),
            pagingSourceFactory = {
                animeDao.getAllAnime()
            }
        ).flow

    override suspend fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity?> {
        return animeDao.getNewestAnimeDataByAnimeId(animeId)
    }

    override suspend fun getAnimeByAnimeId(animeId: Int): AnimeEntity? {
        return withContext(ioDispatcher){
            animeDao.getAnimeByAnimeId(animeId)
        }
    }

    override suspend fun updateAnime(anime: AnimeEntity) {
        animeDao.updateAnime(anime)
    }
}