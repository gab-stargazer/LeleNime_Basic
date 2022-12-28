package com.lelestacia.lelenimexml.core.network

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacterDetail

interface INetworkDataSource {
    suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter>
    suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail
    fun getAiringAnime(): PagingSource<Int, NetworkAnime>
    fun searchAnimeByTitle(query: String, isSafety: Boolean): PagingSource<Int, NetworkAnime>
}