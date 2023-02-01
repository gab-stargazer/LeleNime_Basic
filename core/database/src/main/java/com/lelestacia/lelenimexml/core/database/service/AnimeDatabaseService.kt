package com.lelestacia.lelenimexml.core.database.service

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeDatabaseService @Inject constructor(
    private val animeDao: AnimeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeDatabaseService {
    override suspend fun insertOrReplaceAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.insertOrReplaceAnime(anime = anime)
        }
    }

    override suspend fun getAnimeByAnimeID(animeID: Int): AnimeEntity? {
        return withContext(ioDispatcher) {
            animeDao.getAnimeByAnimeID(animeID = animeID)
        }
    }

    override fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<AnimeEntity> {
        return animeDao.getNewestAnimeDataByAnimeID(animeID = animeID)
    }

    override fun getAnimeHistory(): PagingSource<Int, AnimeEntity> {
        return animeDao.getAnimeHistory()
    }

    override fun getAnimeFavorite(): PagingSource<Int, AnimeEntity> {
        return animeDao.getAnimeFavorite()
    }

    override suspend fun updateAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.updateAnime(anime = anime)
        }
    }
}