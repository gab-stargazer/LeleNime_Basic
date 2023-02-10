package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.home.IHomeAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor(
    animeUseCase: IHomeAnimeUseCase,
    private val commonAnimeUseCase: IAnimeUseCase
) : ViewModel() {
    val topAnime = animeUseCase.getTopAnime().cachedIn(viewModelScope)
    val airingAnime = animeUseCase.getAiringAnime().cachedIn(viewModelScope)
    val upcomingAnime = animeUseCase.getUpcomingAnime().cachedIn(viewModelScope)

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        commonAnimeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}