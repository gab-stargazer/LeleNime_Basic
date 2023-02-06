package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode

interface IAnimeNetworkService {
    fun getAiringAnime(): PagingSource<Int, NetworkAnime>
    fun searchAnimeByTitle(query: String, isSafety: Boolean): PagingSource<Int, NetworkAnime>
    suspend fun getAnimeEpisodesByAnimeID(animeID: Int): List<NetworkEpisode>
    suspend fun getAnimeByAnimeID(animeID: Int): NetworkAnime
}
