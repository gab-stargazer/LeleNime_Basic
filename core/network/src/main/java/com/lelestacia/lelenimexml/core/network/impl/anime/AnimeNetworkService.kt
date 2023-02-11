package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.episodes.EpisodeResponse
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeNetworkService @Inject constructor(
    private val animeAPI: AnimeAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeNetworkService {
    override fun getAiringAnime(): PagingSource<Int, AnimeResponse> =
        SeasonAnimePaging(animeAPI = animeAPI)

    override fun getUpcomingAnime(): PagingSource<Int, AnimeResponse> =
        UpcomingAnimePaging(animeAPI = animeAPI)

    override fun getTopAnime(): PagingSource<Int, AnimeResponse> =
        TopAnimePaging(animeAPI = animeAPI)

    override fun searchAnimeByTitle(
        query: String,
        isSafety: Boolean
    ): PagingSource<Int, AnimeResponse> =
        SearchAnimePaging(
            query = query,
            animeAPI = animeAPI,
            nsfwMode = isSafety
        )

    override suspend fun getAnimeEpisodesByAnimeID(animeID: Int): List<EpisodeResponse> {
        return withContext(ioDispatcher) {
            delay(1000)
            animeAPI.getAnimeEpisodesByAnimeID(animeID = animeID).data
        }
    }

    override fun getAnimeEpisodesWithPagingByAnimeID(animeID: Int): PagingSource<Int, EpisodeResponse> =
        EpisodesPagingSource(
            animeAPI = animeAPI,
            animeID = animeID
        )
    override suspend fun getCharactersByAnimeID(animeID: Int): List<CharacterResponse> {
        delay(1000)
        return animeAPI.getCharactersByAnimeID(animeID = animeID).data
    }

    override suspend fun getAnimeReviewsByAnimeID(animeID: Int): List<GenericReviewResponse> {
        delay(1000)
        return animeAPI.getAnimeReviewsByAnimeID(animeID = animeID).data
    }

    override suspend fun getAnimeRecommendationsByAnimeID(animeID: Int): List<AnimeRecommendationResponse> {
        return animeAPI.getAnimeRecommendationByAnimeID(animeID = animeID).data
    }

    override suspend fun getAnimeByAnimeID(animeID: Int): AnimeResponse {
        return withContext(ioDispatcher) {
            delay(1000)
            animeAPI.getAnimeByAnimeID(animeID = animeID).data
        }
    }
}
