package com.lelestacia.lelenimexml.core.data.impl.episode

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.data.utility.asEpisode
import com.lelestacia.lelenimexml.core.data.utility.asNewEntity
import com.lelestacia.lelenimexml.core.database.impl.episode.IEpisodeDatabaseService
import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val databaseService: IEpisodeDatabaseService,
    private val apiService: IAnimeNetworkService,
    private val errorParserUtil: JikanErrorParserUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEpisodeRepository {

    override fun getEpisodesByAnimeID(animeID: Int): Flow<Resource<List<Episode>>> =
        flow<Resource<List<Episode>>> {
            val localData = databaseService.getEpisodeByAnimeID(animeID = animeID)
            val shouldFetchNetwork = localData.isEmpty()

            if (shouldFetchNetwork) {
                val apiResponse: List<NetworkEpisode> =
                    apiService.getAnimeEpisodesByAnimeID(animeID = animeID)
                val newEpisodeEntities = apiResponse.map { networkEpisode: NetworkEpisode ->
                    networkEpisode.asNewEntity(animeID = animeID)
                }
                databaseService.insertOrUpdateEpisode(episodes = newEpisodeEntities)
            }

            val newData: List<Episode> = databaseService.getEpisodeByAnimeID(animeID = animeID)
                .map { episodeEntity: EpisodeEntity ->
                    episodeEntity.asEpisode()
                }
            emit(Resource.Success(data = newData))
        }.catch { t ->
            when (t) {
                is HttpException -> emit(Resource.Error(null, errorParserUtil(t)))
                else -> emit(Resource.Error(null, "Error: ${t.message}"))
            }
        }.onStart { emit(Resource.Loading) }.flowOn(ioDispatcher)
}
