package com.lelestacia.lelenimexml.core.domain.usecase.anime

import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IAnimeUseCase {
    fun getAnimeByMalID(animeId: Int): Flow<Anime>
    suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime)
    suspend fun updateAnimeFavorite(malID: Int)
    fun isSafeMode(): Boolean
    fun changeSafeMode(isSafeMode: Boolean)
}
