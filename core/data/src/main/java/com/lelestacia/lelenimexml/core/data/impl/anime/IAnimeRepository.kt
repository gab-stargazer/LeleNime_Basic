package com.lelestacia.lelenimexml.core.data.impl.anime

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.review.Review
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    fun getAiringAnime(): Flow<PagingData<Anime>>
    fun getUpcomingAnime(): Flow<PagingData<Anime>>
    fun getTopAnime(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
    fun getAnimeReviewsByAnimeID(animeID: Int): Flow<Resource<List<Review>>>
    fun getAnimeRecommendationsByAnimeID(animeID: Int): Flow<Resource<List<GenericModel>>>
    fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime>
    suspend fun updateAnimeIfNecessary(animeID: Int): Resource<Boolean>
    fun getAnimeHistory(): Flow<PagingData<Anime>>
    suspend fun insertAnimeToHistory(anime: Anime)
    fun getAllFavoriteAnime(): Flow<PagingData<Anime>>
    suspend fun updateAnimeFavorite(malID: Int)
    fun isNsfwMode(): Boolean
    fun changeSafeMode(isNsfw: Boolean)
}
