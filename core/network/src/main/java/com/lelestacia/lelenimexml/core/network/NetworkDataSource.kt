package com.lelestacia.lelenimexml.core.network

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacterDetail
import com.lelestacia.lelenimexml.core.network.source.ApiService
import com.lelestacia.lelenimexml.core.network.source.SearchAnimePaging
import com.lelestacia.lelenimexml.core.network.source.SeasonAnimePaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : INetworkDataSource {
    override suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter> {
        return withContext(ioDispatcher) {
            apiService.getCharactersByAnimeID(animeID).data
        }
    }

    override suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail {
        return withContext(ioDispatcher) {
            apiService.getCharacterDetailByCharacterID(characterID).data
        }
    }

    override fun getAiringAnime(): PagingSource<Int, NetworkAnime> {
        return SeasonAnimePaging(apiService)
    }

    override fun searchAnimeByTitle(
        query: String,
        isSafety: Boolean
    ): PagingSource<Int, NetworkAnime> {
        return SearchAnimePaging(query, apiService, isSafety)
    }
}
