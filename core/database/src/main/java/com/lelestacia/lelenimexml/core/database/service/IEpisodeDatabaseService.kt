package com.lelestacia.lelenimexml.core.database.service

import com.lelestacia.lelenimexml.core.database.entity.episode.EpisodeEntity

interface IEpisodeDatabaseService {
    suspend fun insertOrUpdateEpisodes(episodes: List<EpisodeEntity>)
    suspend fun getEpisodesByAnimeID(animeID: Int): List<EpisodeEntity>
}