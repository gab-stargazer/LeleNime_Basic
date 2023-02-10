package com.lelestacia.lelenimexml.core.domain.usecase.episode

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.episode.Episode
import kotlinx.coroutines.flow.Flow

interface IEpisodeUseCase {
    fun getEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>>
}
