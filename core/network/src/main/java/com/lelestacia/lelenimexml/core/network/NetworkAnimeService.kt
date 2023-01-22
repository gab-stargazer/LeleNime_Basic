package com.lelestacia.lelenimexml.core.network

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.network.source.ApiService
import com.lelestacia.lelenimexml.core.network.source.SearchAnimePaging
import com.lelestacia.lelenimexml.core.network.source.SeasonAnimePaging
import javax.inject.Inject

class NetworkAnimeService @Inject constructor(
    private val apiService: ApiService
) : INetworkAnimeService {
    override fun getAiringAnime(): PagingSource<Int, NetworkAnime> =
        SeasonAnimePaging(apiService = apiService)

    override fun searchAnimeByTitle(
        query: String,
        isSafety: Boolean
    ): PagingSource<Int, NetworkAnime> =
        SearchAnimePaging(
            query = query,
            apiService = apiService,
            isSafety = isSafety
        )
}
