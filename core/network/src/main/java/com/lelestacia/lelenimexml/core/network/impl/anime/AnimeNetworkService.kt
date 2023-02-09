package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import com.lelestacia.lelenimexml.core.network.source.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeNetworkService @Inject constructor(
    private val animeAPI: AnimeAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeNetworkService {
    override fun getAiringAnime(): PagingSource<Int, NetworkAnime> =
        SeasonAnimePaging(animeAPI = animeAPI)

    override fun getUpcomingAnime(): PagingSource<Int, NetworkAnime> =
        UpcomingAnimePaging(animeAPI = animeAPI)

    override fun getTopAnime(): PagingSource<Int, NetworkAnime> =
        TopAnimePaging(animeAPI = animeAPI)

    override fun searchAnimeByTitle(
        query: String,
        isSafety: Boolean
    ): PagingSource<Int, NetworkAnime> =
        SearchAnimePaging(
            query = query,
            animeAPI = animeAPI,
            nsfwMode = isSafety
        )

    override suspend fun getAnimeEpisodesByAnimeID(animeID: Int): List<NetworkEpisode> {
        return withContext(ioDispatcher) {
            delay(1000)
            animeAPI.getAnimeEpisodesByAnimeID(animeID = animeID).data
        }
    }

    override suspend fun getAnimeByAnimeID(animeID: Int): NetworkAnime {
        return withContext(ioDispatcher) {
            delay(1000)
            animeAPI.getAnimeByAnimeID(animeID = animeID).data
        }
    }
}
