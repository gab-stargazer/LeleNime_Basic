package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.network.model.GenericResponse
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse
import com.lelestacia.lelenimexml.core.network.model.pagination.GenericPaginationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaAPI {

    @GET("manga/{id}")
    suspend fun getMangaByMangaID(
        @Path("id") mangaID: Int
    ): GenericResponse<MangaResponse>

    @GET("manga/{id}")
    suspend fun getMangaCharactersByMangaID(
        @Path("id") mangaID: Int
    ): GenericResponse<List<CharacterResponse>>

    @GET("manga/{id}/reviews")
    suspend fun getMangaReviewsByMangaID(
        @Path("id") mangaID: Int
    ): GenericResponse<List<GenericReviewResponse>>

    @GET("manga/{id}/recommendations")
    suspend fun getMangaRecommendationByMangaID(
        @Path("id") mangaID: Int
    ): GenericResponse<List<MangaRecommendationResponse>>

    @GET("manga")
    suspend fun searchMangaByTitle(
        @Query("q") q: String,
        @Query("page") page: Int
    ): GenericPaginationResponse<MangaResponse>

    @GET("manga")
    suspend fun searchMangaByTitle(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("sfw") sfw: Boolean
    ): GenericPaginationResponse<MangaResponse>
}