package com.lelestacia.lelenimexml.core.database.service

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface IAnimeDatabaseService {
    suspend fun insertOrReplaceAnime(anime: AnimeEntity)
    suspend fun getAnimeByAnimeID(animeID: Int): AnimeEntity?
    fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<AnimeEntity>
    fun getAnimeHistory(): PagingSource<Int, AnimeEntity>
    fun getAnimeFavorite(): PagingSource<Int, AnimeEntity>
    suspend fun updateAnime(anime: AnimeEntity)
}