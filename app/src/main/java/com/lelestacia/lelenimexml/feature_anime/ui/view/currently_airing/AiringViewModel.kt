package com.lelestacia.lelenimexml.feature_anime.ui.view.currently_airing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AiringViewModel @Inject constructor(
    animeUseCases: AnimeUseCases
) : ViewModel() {

    val airingAnime = animeUseCases.seasonAnimePagingData()
        .cachedIn(viewModelScope).asLiveData()
}