package com.lelestacia.lelenimexml.core.network.api

import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.network.dto.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.network.dto.anime.GenericPaginationResponse
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

    @GET("anime/{id}/full")
    suspend fun getAnimeById(@Path("id") id: Int): AnimeFUll

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}