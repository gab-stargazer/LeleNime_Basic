package com.lelestacia.lelenimexml.core.database.impl.episode

import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity

interface IEpisodeDatabaseService {
    suspend fun insertOrUpdateEpisode(episodes: List<EpisodeEntity>)
    fun getEpisodeByAnimeID(animeID: Int): List<EpisodeEntity>
}
