package com.lelestacia.lelenimexml.core.data.impl.episode

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.data.utility.asEpisode
import com.lelestacia.lelenimexml.core.data.utility.asNewEntity
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.entity.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.database.service.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.service.IEpisodeDatabaseService
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val animeApiService: IAnimeNetworkService,
    private val animeDatabaseService: IAnimeDatabaseService,
    private val episodeDatabaseService: IEpisodeDatabaseService,
    private val errorParserUtil: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEpisodeRepository {

    /*
     *  The function is designed to use both local data and network data.
     *  it will do the following process:
     *      1. Function will fetch a list of episodes from local data
     *         and check whether it is empty or not
     *      2. Function will fetch the corresponding anime from local data and check:
     *          - Is the anime status On Going or Finished Airing
     *          - Is the anime has more than 1 episode
     *      3. Function will calculate the difference between the last network request based on anime status
     *          - 1 Hour if the anime is On Going
     *          - 1 Day if the anime was finished airing
     *      4. Function will determine whether it should pull new data / new data from network or not
     *      5. If it's making a network call, it will insert the data with the correct
     *         timestamp tobe used again for the latter use
     */

    private var temporaryEpisodeEntities: List<EpisodeEntity> = emptyList()

    override fun getEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>> =
        flow {
            var localEpisodes: List<EpisodeEntity> = episodeDatabaseService
                .getEpisodesByAnimeID(animeID = animeID)
            val isLocalEpisodeEmpty: Boolean = localEpisodes.isEmpty()
            if (!isLocalEpisodeEmpty) {
                temporaryEpisodeEntities = localEpisodes
                val episodes: List<Episode> = localEpisodes.map { it.asEpisode() }
                emit(Resource.Success(data = episodes))
            }

            val oldestUpdate: Long =
                if (isLocalEpisodeEmpty) 0
                else {
                    localEpisodes.minOf { episodes ->
                        (episodes.updatedAt ?: episodes.createdAt).time
                    }
                }

            val anime: AnimeEntity =
                animeDatabaseService.getAnimeByAnimeID(animeID = animeID) as AnimeEntity
            val animeEpisodeCount: Int = anime.episodes ?: 0
            Timber.d("${anime.title} has $animeEpisodeCount episodes")
            val isAnimeOnlyOneEpisode: Boolean = animeEpisodeCount == 1
            val isAnimeOnGoing: Boolean = anime.status == "Currently Airing"

            if (isAnimeOnlyOneEpisode) {
                Timber.d("Anime only has 1 episode, therefor no data should be fetched")
                emit(Resource.Success(data = emptyList()))
                return@flow
            }

            val timeDifference: Long = Date().time - oldestUpdate
            val isDataOutDated: Boolean =
                if (isAnimeOnGoing) {
                    val differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
                    Timber.d("Difference is $differenceInMinutes Minutes")
                    differenceInMinutes.toInt() > 60
                } else {
                    val differenceInHours = TimeUnit.MILLISECONDS.toHours(timeDifference)
                    Timber.d("Difference is $differenceInHours Hours")
                    differenceInHours.toInt() > 24
                }

            val shouldFetchNetwork: Boolean =
                isLocalEpisodeEmpty || isDataOutDated
            if (shouldFetchNetwork) {
                emit(Resource.Loading)
                Timber.d("Episode is being fetch from the network")

                val apiResponse = animeApiService.getAnimeEpisodesByAnimeID(animeID = animeID)
                val newEpisodeEntities: List<EpisodeEntity> = apiResponse
                    .map { networkEpisode: NetworkEpisode ->
                        networkEpisode.asNewEntity(animeID = animeID)
                    }

                localEpisodes =
                    if (isLocalEpisodeEmpty) {
                        Timber.d("Local Episodes replaced")
                        newEpisodeEntities
                    } else {
                        Timber.d("Local Episodes updated")
                        localEpisodes.mapIndexed { index, episodeEntity ->
                            val newEpisode = newEpisodeEntities[index]
                            episodeEntity.copy(
                                titleJapanese = newEpisode.titleJapanese,
                                aired = newEpisode.aired,
                                score = newEpisode.score,
                                updatedAt = Date()
                            )
                        }
                    }

                episodeDatabaseService.insertOrUpdateEpisodes(episodes = localEpisodes)
                val episodes: List<Episode> = localEpisodes.map { it.asEpisode() }
                emit(Resource.Success(data = episodes))
            }
        }.catch { t ->
            val episodes: List<Episode>? =
                if (temporaryEpisodeEntities.isEmpty()) null
                else temporaryEpisodeEntities.map { it.asEpisode() }

            when (t) {
                is HttpException -> emit(
                    Resource.Error(
                        data = episodes,
                        message = errorParserUtil(t)
                    )
                )
                else -> emit(Resource.Error(data = episodes, message = "Error: ${t.message}"))
            }
        }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)
}
