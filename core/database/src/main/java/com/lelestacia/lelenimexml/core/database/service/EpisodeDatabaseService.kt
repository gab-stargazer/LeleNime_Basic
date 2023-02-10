package com.lelestacia.lelenimexml.core.database.service

import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.entity.episode.EpisodeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodeDatabaseService @Inject constructor(
    private val episodeDao: EpisodeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEpisodeDatabaseService {
    override suspend fun insertOrUpdateEpisodes(episodes: List<EpisodeEntity>) {
        withContext(ioDispatcher) {
            episodeDao.insertOrUpdateEpisode(episodes = episodes)
        }
    }

    override suspend fun getEpisodesByAnimeID(animeID: Int): List<EpisodeEntity> {
        return withContext(ioDispatcher) {
            episodeDao.getEpisodeByAnimeID(animeID = animeID)
        }
    }
}