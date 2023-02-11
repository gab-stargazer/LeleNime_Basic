package com.lelestacia.lelenimexml.core.network.impl.manga

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaRecommendationResponse
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse
import com.lelestacia.lelenimexml.core.network.source.MangaAPI
import kotlinx.coroutines.delay
import javax.inject.Inject

class MangaNetworkService @Inject constructor(
    private val mangaAPI: MangaAPI
) : IMangaNetworkService {

    override suspend fun getMangaByMangaID(mangaID: Int): MangaResponse {
        delay(1000)
        return mangaAPI.getMangaByMangaID(mangaID = mangaID).data
    }

    override suspend fun getMangaCharactersByMangaID(mangaID: Int): List<CharacterResponse> {
        delay(1000)
        return mangaAPI.getMangaCharactersByMangaID(mangaID = mangaID).data
    }

    override suspend fun getMangaReviewsByMangaID(mangaID: Int): List<GenericReviewResponse> {
        delay(1000)
        return mangaAPI.getMangaReviewsByMangaID(mangaID = mangaID).data
    }

    override suspend fun getMangaRecommendationByMangaID(mangaID: Int): List<MangaRecommendationResponse> {
        delay(1000)
        return mangaAPI.getMangaRecommendationByMangaID(mangaID = mangaID).data
    }

    override fun searchMangaByTitle(
        title: String,
        sfw: Boolean
    ): PagingSource<Int, MangaResponse> =
        SearchMangaPagingSource(
            mangaAPI = mangaAPI,
            query = title,
            sfw = sfw
        )
}