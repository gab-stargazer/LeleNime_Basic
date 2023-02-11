package com.lelestacia.lelenimexml.core.network.impl.manga

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse

interface IMangaNetworkService {
    suspend fun getMangaByMangaID(mangaID: Int): MangaResponse
    suspend fun getMangaCharactersByMangaID(mangaID: Int): List<CharacterResponse>
    suspend fun getMangaReviewsByMangaID(mangaID: Int): List<GenericReviewResponse>
    suspend fun getMangaRecommendationByMangaID(mangaID: Int): List<MangaRecommendationResponse>
    fun searchMangaByTitle(title: String, sfw: Boolean): PagingSource<Int, MangaResponse>
}