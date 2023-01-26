package com.lelestacia.lelenimexml.core.database.impl.episode

import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodeDatabaseService @Inject constructor(
    private val episodeDao: EpisodeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEpisodeDatabaseService {
    override suspend fun insertOrUpdateEpisode(episodes: List<EpisodeEntity>) {
        withContext(ioDispatcher) {
            episodeDao.insertOrUpdateEpisode(episodes = episodes)
        }
    }

    override fun getEpisodeByAnimeID(animeID: Int): List<EpisodeEntity> {
        return episodeDao.getEpisodeByAnimeID(animeID = animeID)
    }
}
