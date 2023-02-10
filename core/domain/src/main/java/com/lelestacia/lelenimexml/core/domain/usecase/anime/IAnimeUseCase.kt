package com.lelestacia.lelenimexml.core.domain.usecase.anime

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IAnimeUseCase {
    fun getAnimeByMalID(animeID: Int): Flow<Anime>
    suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime)
    suspend fun updateAnimeFavorite(animeID: Int)
    suspend fun updateAnimeIfNecessary(animeID: Int): Resource<Boolean>
    fun isSafeMode(): Boolean
    fun changeSafeMode(isNsfw: Boolean)
}
