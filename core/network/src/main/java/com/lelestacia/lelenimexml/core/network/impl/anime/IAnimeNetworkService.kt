package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.episodes.EpisodeResponse

interface IAnimeNetworkService {
    fun getAiringAnime(): PagingSource<Int, AnimeResponse>
    fun getUpcomingAnime(): PagingSource<Int, AnimeResponse>
    fun getTopAnime(): PagingSource<Int, AnimeResponse>
    fun searchAnimeByTitle(query: String, isSafety: Boolean): PagingSource<Int, AnimeResponse>
    suspend fun getAnimeEpisodesByAnimeID(animeID: Int): List<EpisodeResponse>
    fun getAnimeEpisodesWithPagingByAnimeID(animeID: Int): PagingSource<Int, EpisodeResponse>
    suspend fun getCharactersByAnimeID(animeID: Int): List<CharacterResponse>
    suspend fun getAnimeReviewsByAnimeID(animeID: Int): List<GenericReviewResponse>
    suspend fun getAnimeRecommendationsByAnimeID(animeID: Int): List<AnimeRecommendationResponse>
    suspend fun getAnimeByAnimeID(animeID: Int): AnimeResponse
}
