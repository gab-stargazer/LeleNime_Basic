package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.network.model.*
import com.lelestacia.lelenimexml.core.network.model.anime.*
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.episodes.EpisodeResponse
import com.lelestacia.lelenimexml.core.network.model.pagination.GenericPaginationResponse
import com.lelestacia.lelenimexml.core.network.model.pagination.GenericScrappingPaginationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeAPI {

    @GET("seasons/now")
    suspend fun getCurrentSeason(
        @Query("page") page: Int
    ): GenericPaginationResponse<AnimeResponse>

    @GET("seasons/upcoming")
    suspend fun getUpcomingSeason(
        @Query("page") page: Int
    ): GenericPaginationResponse<AnimeResponse>

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int
    ): GenericPaginationResponse<AnimeResponse>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int
    ): GenericPaginationResponse<AnimeResponse>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("sfw") sfw: Boolean
    ): GenericPaginationResponse<AnimeResponse>

    @GET("anime/{id}")
    suspend fun getAnimeByAnimeID(
        @Path("id") animeID: Int
    ): GenericResponse<AnimeResponse>

    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodesByAnimeID(
        @Path("id") animeID: Int
    ): GenericResponse<List<EpisodeResponse>>

    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodesWithPagingByAnimeID(
        @Path("id") animeID: Int,
        @Query("page") page: Int
    ): GenericScrappingPaginationResponse<EpisodeResponse>

    @GET("anime/{id}/characters")
    suspend fun getCharactersByAnimeID(
        @Path("id") animeID: Int
    ): GenericResponse<List<CharacterResponse>>

    @GET("anime/{id}/reviews")
    suspend fun getAnimeReviewsByAnimeID(
        @Path("id") animeID: Int
    ): GenericResponse<List<GenericReviewResponse>>

    @GET("anime/{id}/recommendations")
    suspend fun getAnimeRecommendationByAnimeID(
        @Path("id") animeID: Int
    ): GenericResponse<List<AnimeRecommendationResponse>>
}
