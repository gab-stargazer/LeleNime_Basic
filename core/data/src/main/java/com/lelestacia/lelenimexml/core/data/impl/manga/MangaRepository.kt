package com.lelestacia.lelenimexml.core.data.impl.manga

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.common.Constant.IS_NSFW
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.*
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.review.Review
import com.lelestacia.lelenimexml.core.network.impl.manga.IMangaNetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val mangaAPI: IMangaNetworkService,
    private val userPreferences: SharedPreferences,
    private val errorParserUtil: JikanErrorParserUtil = JikanErrorParserUtil(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IMangaRepository {

    override fun getMangaByMangaID(mangaID: Int): Flow<Resource<Manga>> =
        flow<Resource<Manga>> {

        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParserUtil(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun getMangaCharactersByMangaID(mangaID: Int): Flow<Resource<List<Character>>> =
        flow<Resource<List<Character>>> {
            val apiResponse = mangaAPI.getMangaCharactersByMangaID(mangaID = mangaID)
            emit(Resource.Success(data = apiResponse.map { it.asNewEntity().asCharacter() }))
        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParserUtil(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun getMangaReviewsByMangaID(mangaID: Int): Flow<Resource<List<Review>>> =
        flow<Resource<List<Review>>> {
            val apiResponse = mangaAPI.getMangaReviewsByMangaID(mangaID = mangaID)
            emit(Resource.Success(data = apiResponse.map { it.asReview() }))
        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParserUtil(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun getMangaRecommendationByMangaID(mangaID: Int): Flow<Resource<List<GenericModel>>> =
        flow<Resource<List<GenericModel>>> {
            val apiResponse = mangaAPI.getMangaRecommendationByMangaID(mangaID = mangaID)
            emit(Resource.Success(data = apiResponse.map { it.entry.asGenericModel() }))
        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParserUtil(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun searchMangaByTitle(title: String): Flow<PagingData<Manga>> =
        Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                mangaAPI.searchMangaByTitle(
                    title = title,
                    sfw = userPreferences.getBoolean(IS_NSFW, false)
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asManga() }
        }
}