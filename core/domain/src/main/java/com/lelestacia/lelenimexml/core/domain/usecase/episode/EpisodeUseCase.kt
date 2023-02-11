package com.lelestacia.lelenimexml.core.domain.usecase.episode

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.model.episode.Episode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeUseCase @Inject constructor(
    private val episodeRepository: IEpisodeRepository
) : IEpisodeUseCase {
    override fun getEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>> =
        episodeRepository.getAnimeEpisodesByAnimeID(animeID = animeID)
}
