package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.network.model.GenericPaginationResponse
import com.lelestacia.lelenimexml.core.network.model.GenericResponse
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeAPI {

    @GET("seasons/now")
    suspend fun getCurrentSeason(@Query("page") page: Int):
        GenericPaginationResponse<List<NetworkAnime>>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int
    ): GenericPaginationResponse<List<NetworkAnime>>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("sfw") sfw: Boolean
    ): GenericPaginationResponse<List<NetworkAnime>>

    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodesByAnimeID(@Path("id") animeID: Int):
        GenericResponse<List<NetworkEpisode>>

    @GET("anime/{id}/characters")
    suspend fun getCharactersByAnimeID(@Path("id") id: Int):
        GenericResponse<List<NetworkCharacter>>
}
