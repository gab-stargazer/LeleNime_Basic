package com.lelestacia.lelenimexml.core.source.remote

import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.model.remote.anime.GenericPaginationResponse
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
    suspend fun getAnimeById(@Path("id") id: Int): com.lelestacia.lelenimexml.core.model.remote.animefull.AnimeFUll

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}