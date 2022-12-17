package com.lelestacia.lelenimexml.feature_anime.ui.season_anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonAnimeViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {

    val airingAnime = animeUseCases.seasonAnimePagingData()
        .cachedIn(viewModelScope).asLiveData()

    fun insertNewOrUpdateLastViewed(anime: Anime) {
        viewModelScope.launch {
            val localAnime = animeUseCases.getAnimeByAnimeId(anime.malId)
            val newData = AnimeMapperUtil.animeToEntity(
                anime = anime,
                isFavorite = localAnime?.isFavorite ?: false
            )
            animeUseCases.insertOrUpdateNewAnimeToHistory(newData)
        }
    }
}