package com.lelestacia.lelenimexml.core.data.impl.episode

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.episode.Episode
import kotlinx.coroutines.flow.Flow

interface IEpisodeRepository {
    fun getAnimeEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>>
    fun getAnimeEpisodesWithPagingByAnimeID(animeID: Int): Flow<PagingData<Episode>>
}
