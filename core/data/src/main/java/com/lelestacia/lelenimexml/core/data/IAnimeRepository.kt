package com.lelestacia.lelenimexml.core.data

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<NetworkAnime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<NetworkAnime>>
    fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity>
    suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity?
    fun getAnimeHistory(): Flow<PagingData<AnimeEntity>>
    suspend fun insertAnimeToHistory(anime: Anime)
    fun getAllFavoriteAnime(): Flow<PagingData<AnimeEntity>>
    suspend fun updateAnimeFavorite(malID: Int)
    fun isSafeMode(): Boolean
    fun changeSafeMode(isSafeMode:Boolean)
}
