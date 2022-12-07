package com.lelestacia.lelenimexml.core.domain.remote

import com.lelestacia.lelenimexml.core.domain.dto.animebyid.AnimeByIdResponse
import com.lelestacia.lelenimexml.core.domain.dto.season.SeasonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanAPI {

    @GET("seasons/now")
    suspend fun getCurrentSeason(@Query("page") page: Int): SeasonResponse

    @GET("anime")
    suspend fun searchAnimeByTitle(@Query("q") q: String, @Query("page") page: Int): SeasonResponse

    @GET("anime/{id}")
    suspend fun getAnimeById(@Path("id") id: Int): AnimeByIdResponse

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}