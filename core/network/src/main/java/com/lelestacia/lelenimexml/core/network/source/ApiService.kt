package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.model.network.GenericPaginationResponse
import com.lelestacia.lelenimexml.core.model.network.GenericResponse
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacterDetail
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("seasons/now")
    suspend fun getCurrentSeason(@Query("page") page: Int): GenericPaginationResponse<List<NetworkAnime>>

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

    @GET("anime/{id}/characters")
    suspend fun getCharactersByAnimeID(@Path("id") id: Int): GenericResponse<List<NetworkCharacter>>

    @GET("characters/{id}/full")
    suspend fun getCharacterDetailByCharacterID(@Path("id") id: Int)
            : GenericResponse<NetworkCharacterDetail>

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}