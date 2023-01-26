package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import com.lelestacia.lelenimexml.core.network.source.SearchAnimePaging
import com.lelestacia.lelenimexml.core.network.source.SeasonAnimePaging
import javax.inject.Inject

class AnimeNetworkService @Inject constructor(
    private val animeAPI: AnimeAPI
) : IAnimeNetworkService {
    override fun getAiringAnime(): PagingSource<Int, NetworkAnime> =
        SeasonAnimePaging(animeAPI = animeAPI)

    override fun searchAnimeByTitle(
        query: String,
        isSafety: Boolean
    ): PagingSource<Int, NetworkAnime> =
        SearchAnimePaging(
            query = query,
            animeAPI = animeAPI,
            isSafety = isSafety
        )

    override suspend fun getAnimeEpisodesByAnimeID(animeID: Int): List<NetworkEpisode> =
        animeAPI.getAnimeEpisodesByAnimeID(animeID = animeID).data
}
