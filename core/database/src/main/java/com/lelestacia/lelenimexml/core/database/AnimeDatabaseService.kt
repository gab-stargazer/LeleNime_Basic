package com.lelestacia.lelenimexml.core.database

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeDatabaseService @Inject constructor(
    private val animeDao: AnimeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeDatabaseService {

    override suspend fun insertOrUpdateAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.insertOrUpdateAnime(anime)
        }
    }

    override suspend fun updateAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.updateAnime(anime)
        }
    }

    override suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity? {
        return withContext(ioDispatcher) {
            animeDao.getAnimeByAnimeId(animeID)
        }
    }

    override fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity> =
        animeDao.getAllAnimeHistory()

    override fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity> =
        animeDao.getNewestAnimeDataByAnimeId(animeID)

    override fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity> =
        animeDao.getAllFavoriteAnime()
}
