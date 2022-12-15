package com.lelestacia.lelenimexml.core.source.remote

import com.lelestacia.lelenimexml.core.model.remote.GenericResponse
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.model.remote.anime.GenericPaginationResponse
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanAPI {

    @GET("seasons/now")
    suspend fun getCurrentSeason(@Query("page") page: Int): GenericPaginationResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int
    ): GenericPaginationResponse<List<AnimeResponse>>

    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("sfw") sfw: Boolean
    ): GenericPaginationResponse<List<AnimeResponse>>

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") id: Int): GenericResponse<List<CharacterResponse>>

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}