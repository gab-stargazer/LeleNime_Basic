package com.lelestacia.lelenimexml.core.data.impl.manga

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.review.Review
import kotlinx.coroutines.flow.Flow

interface IMangaRepository {
    fun getMangaByMangaID(mangaID: Int): Flow<Resource<Manga>>
    fun getMangaCharactersByMangaID(mangaID: Int): Flow<Resource<List<Character>>>
    fun getMangaReviewsByMangaID(mangaID: Int): Flow<Resource<List<Review>>>
    fun getMangaRecommendationByMangaID(mangaID: Int): Flow<Resource<List<GenericModel>>>
    fun searchMangaByTitle(title: String): Flow<PagingData<Manga>>
}