package com.lelestacia.lelenimexml.core.data.impl.episode

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.data.utility.asEpisode
import com.lelestacia.lelenimexml.core.data.utility.asNewEntity
import com.lelestacia.lelenimexml.core.database.impl.anime.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.episode.IEpisodeDatabaseService
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val episodeDatabaseService: IEpisodeDatabaseService,
    private val animeDatabaseService: IAnimeDatabaseService,
    private val apiService: IAnimeNetworkService,
    private val errorParserUtil: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEpisodeRepository {


    /*
     *  If anime is OnGoing the episode data should be no longer than 1 hours
     *  If anime is Finished Airing the episode data should be no longer than 7 days
     */

    override fun getEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>> =
        flow<Resource<List<Episode>>> {
            //Fetch the episodes data locally first to get the oldest update timestamp
            var localEpisodes: List<EpisodeEntity> = episodeDatabaseService
                .getEpisodeByAnimeID(animeID = animeID)
            Timber.d(localEpisodes.toString())
            val isLocalEpisodeEmpty = localEpisodes.isEmpty()
            val oldestUpdate: Long =
                if (isLocalEpisodeEmpty) 0
                else {
                    localEpisodes.minOf { episodes ->
                        (episodes.updatedAt ?: episodes.createdAt).time
                    }
                }

            //Fetch the anime data locally then check the airing status from the anime
            val anime: AnimeEntity =
                animeDatabaseService.getAnimeByAnimeID(animeID = animeID) as AnimeEntity
            val animeEpisodeCount = anime.episodes ?: 0
            val isAnimeHaveMoreThanOneEpisodes = animeEpisodeCount > 1
            val isAnimeOnGoing = anime.status == "Currently Airing"

            if (!isAnimeHaveMoreThanOneEpisodes) {
                Timber.d("Anime only has 1 or less episode, therefor no data should be fetched")
                emit(Resource.Success(data = emptyList()))
                return@flow
            }

            /*
             *  Compare the oldest timestamp and the anime status before making the decision
             *  whether to do network call or not
             */
            val timeDifference: Long = Date().time - oldestUpdate
            val isDataOutDated: Boolean =
                if (isAnimeOnGoing) {
                    val differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
                    Timber.d("Difference is $differenceInMinutes Minutes")
                    differenceInMinutes.toInt() > 60
                } else {
                    val differenceInHours = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24
                    Timber.d("Difference is $differenceInHours Hours")
                    differenceInHours.toInt() > 24
                }

            val shouldFetchNetwork =
                isLocalEpisodeEmpty || isDataOutDated
            if (shouldFetchNetwork) {
                Timber.d("Episode is fetched from the network")
                delay(500)
                val apiResponse = apiService.getAnimeEpisodesByAnimeID(animeID = animeID)
                val newEpisodeEntities = apiResponse.map { networkEpisode: NetworkEpisode ->
                    networkEpisode.asNewEntity(animeID = animeID)
                }

                localEpisodes =
                    if (isLocalEpisodeEmpty) {
                        Timber.d("Local Episodes replaced")
                        newEpisodeEntities
                    } else {
                        Timber.d("Local Episodes updated")
                        localEpisodes.onEachIndexed { index, episodeEntity ->
                            episodeEntity.copy(
                                titleJapanese = newEpisodeEntities[index].titleJapanese,
                                aired = newEpisodeEntities[index].aired,
                                score = newEpisodeEntities[index].score,
                                updatedAt = Date()
                            )
                        }
                    }

                episodeDatabaseService.insertOrUpdateEpisode(episodes = localEpisodes)
            }

            val episodes = localEpisodes.map { it.asEpisode() }
            emit(Resource.Success(data = episodes))
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(data = null, message = errorParserUtil(t)))
                else -> emit(Resource.Error(data = null, message = "Error: ${t.message}"))
            }
        }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)
}
