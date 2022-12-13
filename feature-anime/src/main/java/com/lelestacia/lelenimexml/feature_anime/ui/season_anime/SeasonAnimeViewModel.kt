package com.lelestacia.lelenimexml.feature_anime.ui.season_anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeasonAnimeViewModel @Inject constructor(
    animeUseCases: AnimeUseCases
) : ViewModel() {

    val airingAnime = animeUseCases.seasonAnimePagingData()
        .cachedIn(viewModelScope).asLiveData()
}