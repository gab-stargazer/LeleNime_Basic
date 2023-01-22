package com.lelestacia.lelenimexml.core.network

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime

interface INetworkAnimeService {
    fun getAiringAnime(): PagingSource<Int, NetworkAnime>
    fun searchAnimeByTitle(query: String, isSafety: Boolean): PagingSource<Int, NetworkAnime>
}
