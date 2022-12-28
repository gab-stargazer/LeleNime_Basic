package com.lelestacia.lelenimexml.core.data

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<NetworkAnime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<NetworkAnime>>
    suspend fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity>
    suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity?
    fun getAnimeHistory(): Flow<PagingData<AnimeEntity>>
    suspend fun insertAnimeToHistory(animeEntity: AnimeEntity)
    suspend fun updateAnimeFavorite(malID: Int)
}