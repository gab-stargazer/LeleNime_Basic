package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.pagination.GenericScrappingPaginationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationAPI {

    @GET("recommendations/anime")
    suspend fun getRecentAnimeRecommendation(
        @Query("page") page: Int
    ): GenericScrappingPaginationResponse<GenericRecommendationResponse>

    @GET("recommendation/manga")
    suspend fun getRecentMangaRecommendation(
        @Query("page") page: Int
    ): GenericScrappingPaginationResponse<GenericRecommendationResponse>
}