package com.lelestacia.lelenimexml.core.data.impl.anime

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.common.Constant.IS_NSFW
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.*
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.service.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.review.Review
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeApiService: IAnimeNetworkService,
    private val animeDatabaseService: IAnimeDatabaseService,
    private val userPreferences: SharedPreferences,
    private val errorParser: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimeRepository {

    override fun getAiringAnime(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.getAiringAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun getUpcomingAnime(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.getUpcomingAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.getTopAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        val isNsfw = isNsfwMode()
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                animeApiService.searchAnimeByTitle(query, isNsfw)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }
    }

    override fun getAnimeReviewsByAnimeID(animeID: Int): Flow<Resource<List<Review>>> =
        flow<Resource<List<Review>>> {
            val apiResponse = animeApiService.getAnimeReviewsByAnimeID(animeID = animeID)
            emit(Resource.Success(data = apiResponse.map { it.asReview() }))
        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParser(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun getAnimeRecommendationsByAnimeID(animeID: Int): Flow<Resource<List<GenericModel>>> =
        flow<Resource<List<GenericModel>>> {
            val apiResponse = animeApiService.getAnimeRecommendationsByAnimeID(animeID = animeID)
            val recommendations = apiResponse.map { it.entry.asGenericModel() }
            emit(Resource.Success(data = recommendations))
        }.catch { t ->
            emit(Resource.Error(data = null, message = errorParser(t)))
        }.onStart { emit(Resource.Loading) }
            .flowOn(ioDispatcher)

    override fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime> =
        animeDatabaseService.getNewestAnimeDataByAnimeID(animeID = animeID)
            .map { it.asAnime() }

    override suspend fun updateAnimeIfNecessary(animeID: Int): Resource<Boolean> {
        return withContext(ioDispatcher) {
            val localAnimeEntity: AnimeEntity =
                animeDatabaseService.getAnimeByAnimeID(animeID = animeID) as AnimeEntity
            val timeDifference: Long =
                Date().time - (localAnimeEntity.updatedAt ?: localAnimeEntity.createdAt).time
            val isAnimeDataOutdated =
                if (localAnimeEntity.airing) {
                    val differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
                    Timber.d("Anime ${localAnimeEntity.title} was updated $differenceInMinutes minutes ago")
                    differenceInMinutes.toInt() >= 60
                } else {
                    val differenceInHours = TimeUnit.MILLISECONDS.toHours(timeDifference)
                    Timber.d("Anime ${localAnimeEntity.title} was updated $differenceInHours hours ago")
                    differenceInHours.toInt() >= 24
                }

            if (isAnimeDataOutdated) {
                Timber.d("Anime ${localAnimeEntity.title} is being updated")
                try {
                    val networkAnime = animeApiService.getAnimeByAnimeID(animeID = animeID)
                    val newEntity =
                        networkAnime.asAnime().asNewEntity(isFavorite = localAnimeEntity.isFavorite)
                    animeDatabaseService.updateAnime(
                        newEntity.copy(
                            isFavorite = localAnimeEntity.isFavorite,
                            createdAt = localAnimeEntity.createdAt,
                            updatedAt = Date()
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e.message)
                    Resource.Error(data = false, message = errorParser(e))
                }
            }
            Resource.Success(data = true)
        }
    }


    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                animeDatabaseService.getAnimeHistory()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun insertAnimeToHistory(anime: Anime) {
        withContext(ioDispatcher) {
            val localAnime: AnimeEntity? = animeDatabaseService.getAnimeByAnimeID(anime.malID)
            val isAnimeExist = localAnime != null

            if (isAnimeExist) {
                val updatedHistory: AnimeEntity = (localAnime as AnimeEntity).copy(
                    image = anime.coverImages,
                    trailer = AnimeEntity.Trailer(
                        id = anime.trailer?.youtubeId,
                        url = anime.trailer?.url,
                        image = anime.trailer?.images
                    ),
                    episodes = anime.episodes,
                    status = anime.status,
                    airing = anime.airing,
                    startedDate = anime.startedDate,
                    finishedDate = anime.finishedDate,
                    score = anime.score,
                    scoredBy = anime.scoredBy,
                    rank = anime.rank,
                    lastViewed = Date(),
                    updatedAt = Date()
                )
                animeDatabaseService.updateAnime(updatedHistory)
                return@withContext
            }

            val newAnime = anime.asNewEntity()
            animeDatabaseService.insertOrReplaceAnime(newAnime)
        }
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 30,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                animeDatabaseService.getAnimeFavorite()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override suspend fun updateAnimeFavorite(malID: Int) {
        withContext(ioDispatcher) {
            val anime = animeDatabaseService.getAnimeByAnimeID(animeID = malID)
            anime?.let { oldAnime ->
                val newAnime = oldAnime.copy(
                    isFavorite = !oldAnime.isFavorite,
                    updatedAt = Date()
                )
                animeDatabaseService.updateAnime(newAnime)
            }
        }
    }

    override fun isNsfwMode(): Boolean {
        return userPreferences.getBoolean(IS_NSFW, false)
    }

    override fun changeSafeMode(isNsfw: Boolean) {
        userPreferences
            .edit()
            .putBoolean(IS_NSFW, isNsfw)
            .apply()
    }
}
